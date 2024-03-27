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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public ChatgptApiService(OurBookMapper dao) { // 생성자 수정
        this.dao = dao;
    }

    private final String threadurl = "https://api.openai.com/v1/threads";
    private final String assistantId = "asst_x47fI1xzkEWib5UyiX8OQAKX";
    private String lastThreadId = "thread_QKiC1hWnfw5pG879f5tYZwy4"; // 쓰레드 ID를 저장할 변수

    private int count;

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

    // 예외처리
    private void handleHttpClientErrorException(HttpClientErrorException e) {
        System.err.println("HTTP Client Error: " + e.getStatusCode() + " - " + e.getStatusText());
    }

    // 실행 메소드
    public Map<String, Object> createThreadIfNeeded(String messageContent, String bookname) {
        if (lastThreadId == null) {
            return createThread(messageContent);
        } else {
            System.out.println("Thread already exists. Continuing with existing thread.");
            return sendMessage(messageContent,bookname);
        }
    }

    // 새로운 스레드를 생성하는 메소드
    private Map<String, Object> createThread(String messageContent) {
        HttpHeaders headers = createHeaders();
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", messageContent);
        messages.add(message);
        requestBody.put("messages", messages);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    threadurl, HttpMethod.POST, requestEntity, String.class);
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            String threadId = root.path("id").asText();
            System.out.println("Thread ID: " + threadId);
            lastThreadId = threadId;
            return Map.of("status", "Success", "responseBody", responseBody);
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(e);
            return Map.of("status", "Error");
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("status", "Error");
        }
    }


    // Assistant 실행을 시작하는 메소드
    public void startAssistantRun() {

        HttpHeaders headers = createHeaders();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("assistant_id", assistantId);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        String assistantRunUrl = threadurl + "/" + lastThreadId + "/runs";
        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(assistantRunUrl, requestEntity, Map.class);
            Map<String, String> responseBody = responseEntity.getBody();
            String runId = responseBody.get("id");
            System.out.println("Assistant run started successfully for thread ID: " + lastThreadId);
            System.out.println("Run ID: " + runId);
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> sendMessage(String messageContent, String bookname) {
        try {
            // 모든 책의 상세 정보 가져오기
            List<OurBookDto> allBookDetails = dao.select();

            // 만약 allBookDetails가 비어있다면, 책이 하나도 없다는 메시지를 반환
            if (allBookDetails.isEmpty()) {
                return Map.of("status", "Error", "message", "No books found");
            }

            // assistkeyword가 null인 경우에만 메시지를 전송하도록 설정
            for (OurBookDto book : allBookDetails) {
                if (book.getAssistkeyword() == null) {
                    // 책의 상세 정보를 메시지 content에 추가
                    messageContent += "\n\nBookdetail:\n";
                    messageContent += "Book Name: " + book.getBookname() + "\n";
                    messageContent += "bookdetail: " + book.getBookdetail() + "\n";

                    String url = threadurl + "/" + lastThreadId + "/messages";
                    Map<String, Object> requestBody = new HashMap<>();
                    requestBody.put("role", "user");
                    requestBody.put("content", messageContent);
                    String jsonBody = new Gson().toJson(requestBody);
                    HttpPost post = new HttpPost(url);
                    post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
                    post.setHeader("Authorization", "Bearer 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");
                    post.setHeader("OpenAI-Beta", "assistants=v1");
                    post.setEntity(new StringEntity(jsonBody, ContentType.APPLICATION_JSON));

                    // 20초 대기
                    //   Thread.sleep(20000);

                    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                        HttpResponse response = httpClient.execute(post);
                        int statusCode = response.getStatusLine().getStatusCode();
                        if (statusCode >= 200 && statusCode < 300) {
                            String responseBody = EntityUtils.toString(response.getEntity());
                            Map<String, Object> result = new HashMap<>();
                            result.put("status", "Success");
                            result.put("responseBody", responseBody);
                            startAssistantRun();
                            return result;
                        } else {
                            System.err.println("HTTP Error: " + statusCode + " - " + response.getStatusLine().getReasonPhrase());
                            return Map.of("status", "Error");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Map.of("status", "Error");
                    }
                }
            }
            return Map.of("status", "Error", "message", "All books have non-null assistkeyword");
        } catch (HttpClientErrorException e) {
            Thread.currentThread().interrupt();
            return Map.of("status", "Error", "message", "Thread interrupted");
        }
    }


    // 메시지 응답 확인 메소드
    public OpenAIResponse getLastMessage() {
        String url = threadurl + "/" + lastThreadId + "/messages";

        HttpGet get = new HttpGet(url);
        get.addHeader("Authorization", "Bearer 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");
        get.addHeader("OpenAI-Beta", "assistants=v1");

        ObjectMapper objectMapper = new ObjectMapper();

        try (CloseableHttpClient httpClient = HttpClients.custom().build();
             CloseableHttpResponse response = httpClient.execute(get)) {

            byte[] responseBodyBytes = EntityUtils.toByteArray(response.getEntity());
            String responseBody = new String(responseBodyBytes, StandardCharsets.UTF_8);

            JsonNode rootNode = objectMapper.readTree(responseBody);

            JsonNode dataListNode = rootNode.get("data");
            if (dataListNode != null && dataListNode.isArray()) {
                long latestTimestamp = Long.MIN_VALUE; // 가장 최근에 생성된 메시지의 타임스탬프
                Map<String, Object> latestMessage = null; // 가장 최근에 생성된 메시지

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
                                    return openAIResponse; // 최신 메시지를 찾으면 즉시 반환
                                }
                            }
                        }
                    }
                }
            }

            // 메시지가 없는 경우 빈 OpenAIResponse 반환
            return new OpenAIResponse();

        } catch (Exception e) {
            e.printStackTrace();
            return null; // 예외 발생 시 null 반환
        }
    }

    public void keywordFromAssist() {
        count = 0;
        if (count < 1) {
            Map<String, Object> params = new HashMap<>();
            params.put("AssistKeywordIsNull", true);
            List<OurBookDto> nullList = dao.selectCategory(params);
            if (!nullList.isEmpty()) {
                for (int i = 0; i < 3 && count < 1; i++) {
                    OurBookDto bookDto = nullList.get(count);
                    String bookName = bookDto.getBookname();

                    // OpenAI로부터 최신 메시지를 가져오기
                    OpenAIResponse response = getLastMessage();

                    if (response != null && response.getText() != null && !response.getText().isEmpty()) {
                        bookDto.setAssistkeyword(response.getText());
                        dao.updateAssistKeyword(Collections.singletonList(bookDto));
                        log.info("어시스트 키워드 " + (count + 1) + "번째 업데이트 완료");
                    } else {
                        log.error("OpenAI로부터 메시지를 가져오는데 실패하였습니다.");
                    }
                    count++;
                }
            }
        } else {
            log.info("작업 횟수 제한에 도달하여 작업을 종료합니다.");
        }
    }

}