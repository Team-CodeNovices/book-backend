package com.example.book.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

public class SearchApi {
    public List<String> searchapi(String query, String type) {
        String apiurl = "https://openapi.naver.com/v1/search/book.json?query=" + query + "&display=10";

        // 이하 생략...
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

//        ResponseEntity<String> responseEntity = restTemplate.exchange(apiurl, HttpMethod.GET, entity, String.class);
//        String jsonResponse = responseEntity.getBody();
        String jsonResponse = null;

        List<String> authorsAndPublishers = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode items = root.path("items");

            for (JsonNode item : items) {
                String authorInfo = item.path("author").asText().replace("^", ",");
                String publisherInfo = item.path("publisher").asText();

                // 작가 또는 출판사 정보로 필터링
                if ("author".equalsIgnoreCase(type) && authorInfo.contains(query)) {
                    authorsAndPublishers.add("Author: " + authorInfo + ", Publisher: " + publisherInfo);
                } else if ("publisher".equalsIgnoreCase(type) && publisherInfo.contains(query)) {
                    authorsAndPublishers.add("Author: " + authorInfo + ", Publisher: " + publisherInfo);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return authorsAndPublishers;
    }

}
