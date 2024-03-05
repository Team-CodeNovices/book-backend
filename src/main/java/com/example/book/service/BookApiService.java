package com.example.book.service;

import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OpenAIRequest;
import com.example.book.dto.OpenAIResponse;
import com.example.book.dto.OurBookDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookApiService {

    private final OurBookMapper dao;


    public List<OurBookDto> selectList() throws IOException {
        return dao.select();
    }


    //받은 bookname으로 api 요청하고 Json을 java로 파싱해서 저장하는 메소드
    public List<OurBookDto> getBookDetailsFromApi(String bookName) {
        // 외부 API의 엔드포인트 URL
        String apiUrl = "http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?ttbkey=ttbamdshin1645001&QueryType=Title&MaxResults=1&SearchTarget=Book&output=js&Version=20131101&Query=" + bookName;

        // REST 요청을 보내기 위한 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // API에 GET 요청을 보내고 JSON 형식으로 응답을 받아옴
        String jsonResponse = restTemplate.getForObject(apiUrl, String.class);
        String cleanedJsonResponse = jsonResponse.replaceAll("\\\\", "");

        // JSON 응답을 분석하여 필요한 필드 추출
        ObjectMapper objectMapper = new ObjectMapper();
        List<OurBookDto> bookList = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(cleanedJsonResponse);
            JsonNode items = root.path("item");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    OurBookDto apiData = new OurBookDto();
                    apiData.setImage(item.path("cover").asText());
                    String authorText = item.path("author").asText();
                    String cleanedAuthor = authorText.substring(0, authorText.indexOf('(')).replaceAll("'", "").trim();
                    apiData.setAuthor(cleanedAuthor);
                    String categoryName = item.path("categoryName").asText();
                    String[] categories = categoryName.split(">");
                    String genre = categories[2];
                    apiData.setGenre(genre);
                    apiData.setPublisher(item.path("publisher").asText());
                    apiData.setBookdetail(item.path("description").asText());
                    apiData.setPrice(item.path("priceStandard").asText());
                    apiData.setWritedate(item.path("pubDate").asText());
                    bookList.add(apiData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            OurBookDto apiData = new OurBookDto();
            apiData.setImage("파싱 실패");
            apiData.setAuthor("파싱 실패");
            apiData.setPublisher("파싱 실패");
            apiData.setGenre("파싱 실패");
            apiData.setBookdetail("파싱 실패");
            apiData.setPrice("파싱 실패");
            apiData.setWritedate("파싱 실패");
            apiData.setMainkeyword("파싱 실패");
            bookList.add(apiData);
        }
        return bookList;
    }

    //api Data 받아서 업데이트 처리하는 메소드
    @Scheduled(cron = "0 0/60 * * * *")     //매 시간 정각
    public void updateBooksFromApi() {
        List<OurBookDto> nullList = dao.selectnull();
        if (!nullList.isEmpty()) {
        List<OurBookDto> updatedList = new ArrayList<>();
        int nullCount = 0;
        String nullInfo = "정보없음";
        for (OurBookDto bookDto : nullList) {
            List<OurBookDto> updatedBookDtoList = getBookDetailsFromApi(bookDto.getBookname());
            // api 에서 받아온 정보가 없을 시 nullInfo 삽입
            if (updatedBookDtoList.isEmpty()) {
                bookDto.setImage(nullInfo);
                bookDto.setBookname(bookDto.getBookname());
                bookDto.setAuthor(nullInfo);
                bookDto.setPublisher(nullInfo);
                bookDto.setGenre(nullInfo);
                bookDto.setBookdetail(nullInfo);
                bookDto.setPrice(nullInfo);
                bookDto.setWritedate(nullInfo);
                bookDto.setMainkeyword(nullInfo);
                updatedList.add(bookDto);
                nullCount++;
                log.info(nullCount + "번");
            } else {
                // API에서 가져온 정보로 기존의 도서 정보 업데이트
                for (OurBookDto updatedBookDto : updatedBookDtoList) {
                    bookDto.setImage(updatedBookDto.getImage());
                    bookDto.setBookname(bookDto.getBookname());
                    bookDto.setAuthor(updatedBookDto.getAuthor());
                    bookDto.setPublisher(updatedBookDto.getPublisher());
                    bookDto.setGenre(updatedBookDto.getGenre());
                    bookDto.setBookdetail(updatedBookDto.getBookdetail());
                    bookDto.setPrice(updatedBookDto.getPrice());
                    bookDto.setWritedate(updatedBookDto.getWritedate());
                    bookDto.setMainkeyword(null);
                    updatedList.add(bookDto); // 수정된 정보를 새로운 리스트에 추가
                    log.info("책 정보 업데이트 완료. bookname: " + bookDto.getBookname());
                }
            }
        }
        dao.updateBooksByList(nullList);
        // 업데이트된 책 정보를 데이터베이스에 업데이트
        log.info(nullCount + "개를 제외한" + "업데이트 완료!!");
        }else{
        log.info("업데이트할 리스트가 없어 종료합니다.");
        }
    }

    // OpenAI 요청을 보내는 메소드
    public OpenAIResponse sendOpenAIRequest(String bookName) {
        String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
        String OPENAI_API_KEY = "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";

        RestTemplate restTemplate = new RestTemplate();

        // 요청 본문 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", bookName + "이 책에 관련된 관련 키워드 5개만 알려줘");
        messages.add(message);
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7);

        // HTTP 요청을 위한 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + OPENAI_API_KEY);

        // HTTP 요청 보내기
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    OPENAI_API_URL, HttpMethod.POST, requestEntity, String.class);
            JsonNode root = objectMapper.readTree(responseEntity.getBody());
            String content = root.path("choices").get(0).get("message").get("content").asText();
            OpenAIResponse openAIResponse = new OpenAIResponse();
            openAIResponse.setText(content);
            return openAIResponse;
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Client Error: " + e.getStatusCode() + " - " + e.getStatusText());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

//    @Scheduled(cron = "0 0/60 * * * *") // 매 시간 정각
public void keywordFromApi() {
    List<OurBookDto> nullList = dao.keywordnull();
    if(!nullList.isEmpty()) {

    List<OurBookDto> updatedList = new ArrayList<>();
    for (OurBookDto bookDto : nullList) {
        String bookName = bookDto.getBookname();
        OpenAIResponse response = sendOpenAIRequest(bookName);
        if (response != null) {
            bookDto.setMainkeyword(response.getText());
            updatedList.add(bookDto);
        } else {
            log.error("OpenAI 요청 실패: bookName=" + bookName);
        }
    }
    dao.updateMainKeyword(updatedList);
    log.info("메인 키워드 업데이트 완료");
    }else {
    log.info("비어있는 키워드가 없어 종료합니다.");
    }
}





}
