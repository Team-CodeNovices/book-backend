package com.example.book.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final String threadurl = "https://api.openai.com/v1/threads";
    private final String assistantId = "asst_x47fI1xzkEWib5UyiX8OQAKX";
    private String lastThreadId = "thread_utkA5qtVoYD46IWxSfoaOI1a"; // 쓰레드 ID를 저장할 변수

    private final RestTemplate restTemplate = new RestTemplate();

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer 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");
        headers.set("OpenAI-Beta", "assistants=v1");
        return headers;
    }

    private void handleHttpClientErrorException(HttpClientErrorException e) {
        System.err.println("HP Client Error: " + e.getStatusCode() + " - " + e.getStatusText());
    }

    public void createThreadIfNeeded(String messageContent) {
        if (lastThreadId == null) { // 쓰레드 ID가 없는 경우에만 새로운 쓰레드 생성
            createThread(messageContent);
        } else {
            System.out.println("Thread already exists. Continuing with existing thread.");
            continueThread(messageContent);
        }
    }

    private void createThread(String messageContent) {
        HttpHeaders headers = createHeaders();

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
            listMessagesInThread();
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Client Error: " + e.getStatusCode() + " - " + e.getStatusText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void continueThread(String messageContent) {
        try {
            // Create headers with authentication information
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer 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");
            headers.set("OpenAI-Beta", "assistants=v1");

            // Prepare request body with message content
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, String> message = new HashMap<>();
            message.put("role", "user"); // Set the role to 'user'
            message.put("content", messageContent.trim());
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(message);
            requestBody.put("messages", messages);

            // Construct the URL for sending the message to the thread
            String continueThreadUrl = threadurl + "/" + lastThreadId + "/messages";

            // Send the request to the server
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    continueThreadUrl, HttpMethod.POST, new HttpEntity<>(requestBody, headers), String.class);

            // Check if the request was successful
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                System.out.println("Message added to the thread successfully.");

                // Process additional information in the response, if available
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(responseEntity.getBody());
                if (root.has("data")) {
                    JsonNode dataNode = root.path("data");
                    // Extract and handle any additional information from the response
                } else {
                    System.err.println("No additional data found in the response.");
                }

                // List messages in the thread and start assistant run
                listMessagesInThread();
                startAssistantRun();
            } else {
                // Handle unsuccessful request
                System.err.println("HTTP Error: " + responseEntity.getStatusCodeValue() + " - " + responseEntity.getStatusCode().getReasonPhrase());
                System.err.println("Response body: " + responseEntity.getBody());
            }
        } catch (HttpClientErrorException e) {
            // Handle HTTP client errors
            System.err.println("HTTP Client Error: " + e.getRawStatusCode() + " - " + e.getStatusText());
            System.err.println("Response body: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
    }


    public void startAssistantRun() {
        HttpHeaders headers = createHeaders();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("assistant_id", assistantId);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        String assistantRunUrl = threadurl + "/" + lastThreadId + "/runs";
        try {
            System.out.println("Sending request to start assistant run...");
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

    public void listMessagesInThread() {
        HttpHeaders headers = createHeaders();
        String threadMessagesUrl = threadurl + "/" + lastThreadId + "/messages";

        try {
            System.out.println("Sending request to list messages in thread...");
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    threadMessagesUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
            String responseBody = responseEntity.getBody();
            System.out.println("Messages in thread " + lastThreadId + ":");
            System.out.println(responseBody);
        } catch (HttpClientErrorException e) {
            handleHttpClientErrorException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        ChatgptApiService threadApiService = new ChatgptApiService();
        threadApiService.createThreadIfNeeded("Hello, GPT!"); // 쓰레드 생성 또는 필요한 경우 기존 쓰레드 사용
    }
}
