package com.example.book.service;

import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OpenAIRequest;
import com.example.book.dto.OpenAIResponse;
import com.example.book.dto.OurBookDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.lang.model.util.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
public class BookApiService {

    private final OurBookMapper dao;
    @Value("${is.server}")
    private boolean isServer;
    @Value("${naver.id}")
    private String NaverId;
    @Value("${naver.secret}")
    private String NaverSecret;
    @Value("${chat.apikey}")
    private String OPENAI_API_KEY;
    private int count = 0;  //횟수제한

    @Autowired
    public BookApiService(OurBookMapper dao, @Value("${is.server}") boolean isServer,
                          @Value("${naver.id}") String NaverId,
                          @Value("${naver.secret}") String NaverSecret,
                          @Value("${chat.apikey}") String OPENAI_API_KEY) {
        this.dao = dao;
        this.isServer = isServer;
        this.NaverId = NaverId;
        this.NaverSecret = NaverSecret;
        this.OPENAI_API_KEY = OPENAI_API_KEY;
    }

    //네이버 api Data 받아서 업데이트 처리하는 메소드
    @Scheduled(cron = "0 0/30 * * * *") // 매 30분마다 반복
    public void updateBooksFromApi() throws IOException {
        if (isServer) {     //서버에서 일때만 스케줄러가 사용할수 있게
            List<OurBookDto> nullList = dao.selectnull();
            if (!nullList.isEmpty()) {
                for (OurBookDto bookDto : nullList) {
                    List<OurBookDto> updatedBookDtoList = getNaverApi(bookDto.getBookname());
                    for (OurBookDto updatedBookDto : updatedBookDtoList) {
                        List<OurBookDto> infoList = getNaverCrawling(updatedBookDto.getLink());
                        if (!infoList.isEmpty()) {
                            OurBookDto infoDto = infoList.get(0);
                            OurBookDto updatedDto = new OurBookDto();
                            updatedDto.setLink(updatedBookDto.getLink());
                            updatedDto.setImage(updatedBookDto.getImage());
                            updatedDto.setBookname(bookDto.getBookname());
                            updatedDto.setAuthor(updatedBookDto.getAuthor());
                            updatedDto.setPublisher(updatedBookDto.getPublisher());
                            updatedDto.setGenre(infoDto.getGenre());
                            updatedDto.setContents(infoDto.getContents());
                            updatedDto.setBookdetail(updatedBookDto.getBookdetail());
                            updatedDto.setAuthordetail(infoDto.getAuthordetail());
                            updatedDto.setPrice(updatedBookDto.getPrice());
                            updatedDto.setWritedate(updatedBookDto.getWritedate());
                            updatedDto.setMainkeyword(null);
                            updatedDto.setAssistkeyword(null);
                            dao.updateBooksByList(Collections.singletonList(updatedDto));
                            log.info("책 정보 업데이트 완료. bookname: " + bookDto.getBookname());
                        }
                    }
                }
                log.info("업데이트 완료!!");
            } else {
                log.info("업데이트할 리스트가 없어 종료합니다.");
            }
        } else {
            log.info("서버가 아니므로 작업을 스킵합니다.");
        }
    }


    //네이버 크롤링하는 메소드
    public static List<OurBookDto> getNaverCrawling(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        List<OurBookDto> list = new ArrayList<>();

        //장르
        Element genreElement = doc.select("[class*=bookCatalogTop_category__]").last();
        String genre = (genreElement != null && !genreElement.text().isEmpty()) ? genreElement.text() : "정보 없음";
        // 목차
        Element contentsElement = doc.select("[class*=infoItem_data_text__]").last();
        String contents = (contentsElement != null && !contentsElement.text().isEmpty()) ? contentsElement.text() : "정보 없음";
        // 저자 소개
        Element authorDetailElement = doc.select("[class*=authorIntroduction_introduce_text__]").first();
        String authorDetail = (authorDetailElement != null && !authorDetailElement.text().isEmpty()) ? authorDetailElement.text() : "정보 없음";

        // OurBookDto 객체에 정보 저장
        OurBookDto bookDto = new OurBookDto();
        bookDto.setGenre(genre);
        bookDto.setContents(contents);
        bookDto.setAuthordetail(authorDetail);

        list.add(bookDto);

        return list;
    }

    //네이버 api 받아오는 메소드
    public List<OurBookDto> getNaverApi(String bookName) throws IOException {
        // 외부 API의 엔드포인트 URL
        String apiUrl = "https://openapi.naver.com/v1/search/book.json?display=1&query=" + bookName;

        // REST 요청을 보내기 위한 RestTemplate 객체 생성
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", NaverId);
        headers.set("X-Naver-Client-Secret", NaverSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API에 GET 요청을 보내고 JSON 형식으로 응답을 받아옴
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        String jsonResponse = responseEntity.getBody();

        // JSON 응답을 분석하여 필요한 필드 추출
        ObjectMapper objectMapper = new ObjectMapper();
        List<OurBookDto> bookList = new ArrayList<>();
        JsonNode root = objectMapper.readTree(jsonResponse);
        JsonNode items = root.path("items");

        for (JsonNode item : items) {
            OurBookDto apiData = new OurBookDto();
            String link = item.path("link").asText();
            apiData.setLink(link != null && !link.isEmpty() ? link : "정보 없음");
            String image = item.path("image").asText();
            apiData.setImage(image != null && !image.isEmpty() ? image : "정보 없음");
            String author = item.path("author").asText().replace("^", ",");
            apiData.setAuthor(author != null && !author.isEmpty() ? author : "정보 없음");
            String publisher = item.path("publisher").asText();
            apiData.setPublisher(publisher != null && !publisher.isEmpty() ? publisher : "정보 없음");
            String description = item.path("description").asText();
            apiData.setBookdetail(description != null && !description.isEmpty() ? description : "정보 없음");
            String price = item.path("discount").asText();
            apiData.setPrice(price != null && !price.isEmpty() ? price : "정보 없음");
            String pubdate = item.path("pubdate").asText();
            apiData.setWritedate(pubdate != null && !pubdate.isEmpty() ? pubdate : "정보 없음");

            bookList.add(apiData);
        }
        return bookList;
    }

    // OpenAI 요청을 보내는 메소드
    public OpenAIResponse sendOpenAIRequest(String bookName) {
        String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

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

    // chat api로 관련 키워드 저장하는 메소드
    @Scheduled(cron = "0 */5 * * * *") // 5분마다 작동
    public void keywordFromApi() {
        if (isServer) {     //서버에서 일때만 스케줄러가 사용할수 있게
            count = 0;
            if (count < 3) {
                Map<String, Object> params = new HashMap<>();
                params.put("mainKeywordIsNull", true);
                List<OurBookDto> nullList = dao.selectCategory(params);
                if (!nullList.isEmpty()) {
                    for (int i = 0; i < 3 && count < 3; i++) {
                        OurBookDto bookDto = nullList.get(count);
                        String bookName = bookDto.getBookname();
                        OpenAIResponse response = sendOpenAIRequest(bookName);
                        if (response != null && response.getText() != null && !response.getText().isEmpty()) {
                            bookDto.setMainkeyword(response.getText());
                            dao.updateMainKeyword(Collections.singletonList(bookDto));
                            log.info("메인 키워드 " + (count + 1) + "번째 업데이트 완료");
                        } else {
                            log.error("OpenAI 요청 실패: bookName=" + bookName);
                        }
                        count++;
                    }
                }
            } else {
                log.info("작업 횟수 제한에 도달하여 작업을 종료합니다.");
            }
        } else {
            log.info("서버가 아니므로 작업을 스킵합니다.");
        }
    }

    //이전 알라딘 api(사용x)
    public List<OurBookDto> getAladinApi(String bookName) throws IOException {
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
                apiData.setPrice(item.path("priceStandard").asText());
                apiData.setWritedate(item.path("pubDate").asText());
                bookList.add(apiData);
            }
        }
        return bookList;
    }

}
