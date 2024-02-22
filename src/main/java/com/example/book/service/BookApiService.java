package com.example.book.service;

import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OurBookDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
//                    apiData.setAuthor(item.path("author").asText());
                    String authorText = item.path("author").asText();
                    String cleanedAuthor = authorText.substring(0, authorText.indexOf('(')).replaceAll("'", "").trim();
                    apiData.setAuthor(cleanedAuthor);
                    
                    apiData.setPublisher(item.path("publisher").asText());
                    apiData.setBookdetail(item.path("description").asText());
                    apiData.setPrice(item.path("priceStandard").asText());
                    apiData.setWritedate(item.path("pubDate").asText());
                    bookList.add(apiData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    //api Data 받아서 업데이트 처리하는 메소드
    public void updateBooksFromApi() {
        List<OurBookDto> nullList = dao.selectnull();
        List<OurBookDto> updatedList = new ArrayList<>();
        int nullCount = 0;
        String nullInfo = "정보없음";
        for (OurBookDto bookDto : nullList) {
            List<OurBookDto> updatedBookDtoList = getBookDetailsFromApi(bookDto.getBookname());
            if (updatedBookDtoList.isEmpty()) {
                bookDto.setBookname(bookDto.getBookname());
                bookDto.setAuthor(nullInfo);
                bookDto.setPublisher(nullInfo);
                bookDto.setGenre(nullInfo);
                bookDto.setBookdetail(nullInfo);
                bookDto.setPrice(nullInfo);
                bookDto.setWritedate(nullInfo);
                updatedList.add(bookDto);
                nullCount++;
                log.info(nullCount + "번");
            } else {
                // API에서 가져온 정보로 기존의 도서 정보 업데이트
                for (OurBookDto updatedBookDto : updatedBookDtoList) {
                    bookDto.setBookname(bookDto.getBookname());
                    bookDto.setAuthor(updatedBookDto.getAuthor());
                    bookDto.setPublisher(updatedBookDto.getPublisher());
                    bookDto.setGenre(nullInfo); // 장르 정보는 업데이트하지 않음
                    bookDto.setBookdetail(updatedBookDto.getBookdetail());
                    bookDto.setPrice(updatedBookDto.getPrice());
                    bookDto.setWritedate(updatedBookDto.getWritedate());
                    updatedList.add(bookDto); // 수정된 정보를 새로운 리스트에 추가
                    log.info("책 정보 업데이트 완료. bookname: " + bookDto.getBookname());
                }
            }
        }
        // 기존 리스트에 새로운 리스트의 내용을 추가
        nullList.addAll(updatedList);
        // 업데이트된 책 정보를 데이터베이스에 업데이트
        dao.updateBooksByList(nullList);
        log.info(nullCount + "개를 제외한" + "업데이트 완료!!");
    }


}
