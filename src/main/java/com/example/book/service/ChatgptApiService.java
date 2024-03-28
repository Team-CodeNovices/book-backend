package com.example.book.service;

import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OpenAIResponse;
import com.example.book.dto.OurBookDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class ChatgptApiService {

    private final OurBookMapper dao;
    private final boolean isServer;

    @Autowired
    public ChatgptApiService(OurBookMapper dao,@Value("${is.server:false}") boolean isServer) { // 생성자 수정
        this.dao = dao;
        this.isServer = isServer;
    }

    private final String threadurl = "https://api.openai.com/v1/threads";
    private final String assistantId = "asst_x47fI1xzkEWib5UyiX8OQAKX";
    private String lastThreadId = "thread_QKiC1hWnfw5pG879f5tYZwy4"; // 쓰레드 ID를 저장할 변수


    // 공통된 RestTemplate
    private final RestTemplate restTemplate = new RestTemplate();


    // 공통된 요청헤더
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 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");
        headers.set("OpenAI-Beta", "assistants=v1");
        return headers;
    }

    // Assistant 메시지 보내기 및 실행하는 메소드
    public Map<String, Object> sendMessage(String bookname, String bookdetail) {

            String bookDetailMessage = "\n\nBookdetail:\n" +
                    "Book Name: " + bookname + "\n" +
                    "bookdetail: " + bookdetail + "\n" +
                    "내가 너에게 준 bookname하고 bookdetail에서 준 정보로 키워드 5개만 뽑아줘 그리고 키워드만 보여줘"
                    ;

            HttpHeaders headers = createHeaders();
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("role", "user");
            requestBody.put("content", bookDetailMessage);

            String messageUrl = threadurl + "/" + lastThreadId + "/messages";
            String assistantRunUrl = threadurl + "/" + lastThreadId + "/runs";

            try {
                // 메시지 전송
                ResponseEntity<String> messageResponseEntity = restTemplate.exchange(messageUrl, HttpMethod.POST,
                        new HttpEntity<>(requestBody, headers), String.class);
                String messageResponseBody = messageResponseEntity.getBody();

                // Assistant 실행
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(assistantRunUrl,
                        new HttpEntity<>(Collections.singletonMap("assistant_id", assistantId), headers), Map.class);
                Map<String, String> responseBody = responseEntity.getBody();
                String runId = responseBody.get("id");
                System.out.println("Assistant run started successfully for thread ID: " + lastThreadId);
                System.out.println("Run ID: " + runId);

                return Map.of("status", "Success", "responseBody", messageResponseBody);
            } catch (HttpClientErrorException e) {
                return Map.of("status", "Error");
            } catch (Exception e) {
                log.error("Exception occurred: {}", e.getMessage());
                return Map.of("status", "Error");
            }
        }

    // 메시지 응답 확인 메소드
    public OpenAIResponse getLastMessage() {
        String url = threadurl + "/" + lastThreadId + "/messages";
        HttpHeaders headers = createHeaders();
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode dataListNode = rootNode.get("data");
            if (dataListNode != null && dataListNode.isArray()) {
                long latestTimestamp = Long.MIN_VALUE;
                Map<String, Object> latestMessage = null;
                for (JsonNode itemNode : dataListNode) {
                    if ("assistant".equals(itemNode.get("role").asText())) {
                        long createdAtTimestamp = Long.parseLong(itemNode.get("created_at").asText());
                        if (createdAtTimestamp > latestTimestamp) {
                            latestTimestamp = createdAtTimestamp;
                            latestMessage = new HashMap<>();
                            latestMessage.put("created_at", itemNode.get("created_at").asText());
                            latestMessage.put("thread_id", itemNode.get("thread_id").asText());
                            latestMessage.put("role", itemNode.get("role").asText());

                            JsonNode contentNode = itemNode.get("content");
                            if (contentNode != null && contentNode.isArray() && contentNode.size() > 0) {
                                JsonNode textNode = contentNode.get(0).get("text");
                                if (textNode != null && textNode.has("value")) {
                                    String value = textNode.get("value").asText();
                                    latestMessage.put("value", value);
                                    OpenAIResponse openAIResponse = new OpenAIResponse();
                                    openAIResponse.setText(value);
                                    return openAIResponse;
                                }
                            }
                        }
                    }
                }
                return new OpenAIResponse();
            }
        } catch (HttpClientErrorException e) {
            log.error("HTTP Client Error: {} - {}", e.getStatusCode(), e.getStatusText());
        } catch (Exception e) {
            log.error("Exception occurred: {}", e.getMessage());
        }
        return null;
    }

    // 키워드 업데이트
    @Scheduled(cron = "0 */2 * * * *")
    public void updateAssistKeywords() {
        int count = 0;
        List<OurBookDto> books = dao.assistnull();
        if (books.isEmpty()) {
            log.info("No books found with null assist keyword.");
            return;
        }

        for (OurBookDto book : books) {
            if (count >= 1) {
                log.info("Reached the limit of 1 updates.");
                break;
            }

            try {
                sendMessage(book.getBookname(),book.getBookdetail());
                Thread.sleep(10000);
                OpenAIResponse response = getLastMessage();
                if (response != null && response.getText() != null && !response.getText().isEmpty()) {
                    book.setAssistkeyword(response.getText());
                    dao.updateAssistKeyword(Collections.singletonList(book));
                    log.info("Updated assist keyword for book: {}", book.getBookname());
                    count++;
                } else {
                    log.error("Failed to retrieve message from OpenAI.");
                }
            } catch (Exception e) {
                log.error("Exception occurred while updating assist keyword: {}", e.getMessage());
            }  if (isServer) {
                log.info("Running on server.");
            } else {
                log.info("Running on local environment.");
            }
        }
    }
}