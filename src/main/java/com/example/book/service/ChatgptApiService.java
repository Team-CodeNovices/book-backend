package com.example.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatgptApiService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.threadurl}")
    private String threadurl = "https://api.openai.com/v1/threads";

    @Value("${openai.assistant.id}")
    private String assistantId = "asst_x47fI1xzkEWib5UyiX8OQAKX";

    private String lastThreadId; // 마지막으로 생성된 쓰레드 ID를 저장하는 변수
    private String lastRunId; // 마지막으로 생성된 run ID를 저장하는 변수

    public void createThread(String messageContent) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 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"); // OpenAI API 키 설정
        headers.set("OpenAI-Beta", "assistants=v1");

        // 요청 바디 설정
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", messageContent); // 메시지 내용을 메소드 매개변수로 설정

        messages.add(message);
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    threadurl, HttpMethod.POST, requestEntity, String.class);
            String responseBody = responseEntity.getBody();
            System.out.println("Thread created successfully.");

            // ObjectMapper를 사용하여 JSON 응답을 파싱하여 thread ID 추출
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            String threadId = root.path("id").asText();
            System.out.println("Thread ID: " + threadId);

            lastThreadId = threadId; // 쓰레드 ID를 클래스 변수에 저장

            // 새로운 메서드 호출 - 스레드의 메시지 목록 출력
            listMessagesInThread(threadId);
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Client Error: " + e.getStatusCode() + " - " + e.getStatusText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listMessagesInThread(String threadId) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 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"); // OpenAI API 키 설정
        headers.set("OpenAI-Beta", "assistants=v1");

        // 스레드의 메시지 목록을 가져오는 엔드포인트 URL
        String threadMessagesUrl = threadurl + "/" + threadId + "/messages";

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    threadMessagesUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            String responseBody = responseEntity.getBody();
            System.out.println("Messages in thread " + threadId + ":");
            System.out.println(responseBody);
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Client Error: " + e.getStatusCode() + " - " + e.getStatusText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToThread(String threadId, String messageContent) {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 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");
        headers.set("OpenAI-Beta", "assistants=v1");

        // 요청 바디 설정
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("role", "user");
        requestBody.put("content", messageContent);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        String sendMessageUrl = threadurl + "/" + threadId + "/messages";

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    sendMessageUrl, HttpMethod.POST, requestEntity, String.class);
            String responseBody = responseEntity.getBody();
            System.out.println("Message sent successfully to thread ID: " + threadId);
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Client Error: " + e.getStatusCode() + " - " + e.getStatusText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startAssistantRun(String assistantId) {
        if (lastThreadId == null) {
            System.out.println("No thread ID available.");
            return;
        }

        // 쓰레드를 생성한 후에 lastRunId를 설정합니다.
        createThread("Initializing thread for assistant run");

        RestTemplate restTemplate = new RestTemplate();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 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");
        headers.set("OpenAI-Beta", "assistants=v1");

        // 요청 바디 설정
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("assistant_id", assistantId); // 올바른 assistantId를 사용합니다.
        requestBody.put("run_id", lastRunId); // 마지막으로 생성된 run ID 사용

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // 스레드의 assistant run 시작 엔드포인트 URL
        String assistantRunUrl = threadurl + "/" + lastThreadId + "/runs";

        try {
            // POST 요청 보내기
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    assistantRunUrl, HttpMethod.POST, requestEntity, String.class);
            System.out.println("Assistant run started successfully for thread ID: " + lastThreadId);
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Client Error: " + e.getStatusCode() + " - " + e.getStatusText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChatgptApiService threadApiService = new ChatgptApiService();
        threadApiService.createThread("Hello, what is AI?");

        // 스레드를 생성한 후에 startAssistantRun 메서드를 호출하여 lastRunId를 설정합니다.
        threadApiService.startAssistantRun(threadApiService.assistantId);
    }
}
