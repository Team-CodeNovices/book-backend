package com.example.book.service;

import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OurBookDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ChatgptApiService {

    private final  OurBookMapper dao;

    @Autowired
    public ChatgptApiService(OurBookMapper dao) { // 생성자 수정
        this.dao = dao;
    }

    private final String threadurl = "https://api.openai.com/v1/threads";
    private final String assistantId = "asst_x47fI1xzkEWib5UyiX8OQAKX";
    private String lastThreadId = "thread_cFuoq0SwHXGQlMdLrp12CSqs"; // 쓰레드 ID를 저장할 변수

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
            return sendMessage(messageContent);
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

    public Map<String, Object> sendMessage(String messageContent) {
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
                    messageContent += "Author: " + book.getAuthor() + "\n";
                    messageContent += "Publisher: " + book.getPublisher() + "\n";
                    messageContent += "bookdetail: " + book.getBookdetail() + "\n";

                    // 이 부분에서 saveToDatabase 메소드 호출
                    Map<String, Object> saveResult = saveToDatabase(lastThreadId);
                    if (saveResult.get("status").equals("Success")) {
                        System.out.println("Data saved to database successfully");
                    } else {
                        System.out.println("Failed to save data to database");
                    }

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
    public Map<String, Object> getLastMessage(String threadId) {
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
            List<Map<String, Object>> messages = new ArrayList<>();

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
                                }
                            }
                        }
                    }
                }

                if (latestMessage != null) {
                    messages.add(latestMessage);
                }
            }

            Map<String, Object> map = new HashMap<>();
            map.put("status", "Success");
            map.put("messages", messages);

            return map;

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = new HashMap<>();
            map.put("status", "Failed");
            map.put("content", e.getMessage());
            return map;
        }
    }

    // 키워들 저장 메소드
    public Map<String, Object> saveToDatabase(String threadId) {
        // 가져온 내용을 데이터베이스에 저장하는 로직을 구현합니다.
        // 우선 가져온 내용을 변환하여 적절한 데이터 구조로 만듭니다.
        Map<String, Object> lastMessage = getLastMessage(threadId);
        List<Map<String, Object>> messages = (List<Map<String, Object>>) lastMessage.get("messages");

        List<OurBookDto> bookDtoList = new ArrayList<>(); // OurBookDto 객체를 저장할 리스트 생성

        for (Map<String, Object> message : messages) {
            String value = (String) message.get("value");
            // 가져온 내용을 mainkeyword에 매핑되는 형식으로 변환합니다.
            // 예를 들어, OurBookDto 객체를 생성하여 mainkeyword에 해당하는 필드에 value를 설정합니다.
            OurBookDto bookDto = new OurBookDto();
            bookDto.setAssistkeyword(value);

            bookDtoList.add(bookDto); // 생성된 OurBookDto 객체를 리스트에 추가
        }

        // 데이터베이스에 저장합니다.
        try {
            dao.updateAssistKeyword(bookDtoList); // OurBookDto 객체 리스트를 전달하여 데이터베이스에 저장
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastMessage;
    }






}