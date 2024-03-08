package com.example.book.service;

import com.example.book.dao.OurBookMapper;
import com.example.book.dto.AladinDto;
import com.example.book.dto.OurBookDto;
import com.example.book.dto.Yes24Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingWorker {

    private final OurBookMapper dao;
    @Async
    public void getAladinAnother() throws IOException {
        List<AladinDto> list = getAladinData(1, 20);
        insertAladinData(list);
    }

    public static List<AladinDto> getAladinData(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.aladin.co.kr/";
        List<AladinDto> list = new ArrayList<>();
        int ranking = 1;
        int totalpage = stopP; // 999위까지 페이지 갯수
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/shop/common/wbest.aspx?BestType=Bestseller&BranchType=1&CID=0&cnt=1000&SortOrder=1&page=" + page;
            Document doc = Jsoup.connect(pageUrl).get();

            Elements bookBoxes = doc.select(".ss_book_box");
            for (Element bookBox : bookBoxes) {
//                String itemid = bookBox.attr("itemid"); // itemid 추출
                String bookTitle = bookBox.select(".bo3").text(); // 책 이름 추출
                String price = bookBox.select(".ss_p2").text(); // 가격 추출

                // 작가, 출판사, 출판일자 정보가 포함된 요소를 찾습니다.
                // 이 정보는 페이지의 구조에 따라 다를 수 있으므로, 예제에서는 간단화를 위해 직접적인 선택자를 사용합니다.
                // 실제 구조에 맞게 선택자를 조정해야 할 수 있습니다.
                Element infoElement = bookBox.selectFirst(".bo3").parent().nextElementSibling(); // 가정: 작가 및 출판사 정보가 bo3의 부모 요소의 다음 형제 요소에 위치
                String authorAndPublisher = infoElement.text(); // 작가 및 출판사 정보 추출
                String image = bookBox.select(".front_cover").attr("src");
                // 작가 및 출판사 정보 분리 (실제 페이지 구조에 따라 다를 수 있음)
                String[] parts = authorAndPublisher.split("\\|"); // "|"를 구분자로 사용하여 분리
                String author = parts.length > 0 ? parts[0].trim() : ""; // 첫 번째 부분을 작가 정보로 사용
                String publisher = parts.length > 1 ? parts[1].trim() : ""; // 두 번째 부분을 출판사 정보로 사용
                String publishDate = parts.length > 2 ? parts[2].trim() : ""; // 세 번째 부분을 출판일자로 사용
                //log.info("rank-list : " + bookTitle + price + author + publisher + publishDate);

                AladinDto dto = new AladinDto(
                        ranking,
                        image,
                        bookTitle,
                        author,
                        publisher,
                        price,
                        publishDate
                );
                list.add(dto);
                ranking++;
            }
        }
        return list;
    }
    public void insertAladinData(List<AladinDto> list1) throws IOException {
        List<OurBookDto> existBooks = dao.select();
        List<OurBookDto> list2 = new ArrayList<>();

        for (AladinDto aladinDto : list1) {

            // 전체 도서 중에서 이미 존재하지 않는 도서는 list2에 추가
            String finalBookNameText = aladinDto.getBookname();
//            log.info(finalBookNameText);
            boolean exist = existBooks.stream().anyMatch(existingBook -> existingBook.getBookname().equals(finalBookNameText));
            if (!exist) {
                OurBookDto dto2 = OurBookDto.builder()
                        .bookname(finalBookNameText)
                        .build();
                list2.add(dto2);
            }
        }
        // 여러번 insert 되는 문제를 막기위해 비동기로 진행
        if (!list2.isEmpty()) {
            log.info("insert 시작");
            dao.insert(list2);
            log.info("ourbook에 없는 정보를 insert 하였습니다.");
        } else {
            log.info("추가된 책이 없습니다.");
        }
    }

    @Async
    public void getYes24Another() throws IOException {
        List<Yes24Dto> list = getYes24DataNew(1, 9);
        insertYes24Data(list);
    }

    //ourbook과 비교하여 없는 데이터 insert 하는 메소드
    public void insertYes24Data(List<Yes24Dto> list1) throws IOException {
        List<OurBookDto> existBooks = dao.select();
        List<OurBookDto> list2 = new ArrayList<>();

        for (Yes24Dto yes24Dto : list1) {

            // 전체 도서 중에서 이미 존재하지 않는 도서는 list2에 추가
            String finalBookNameText = yes24Dto.getBookname();
            boolean exist = existBooks.stream().anyMatch(existingBook -> existingBook.getBookname().equals(finalBookNameText));
            if (!exist) {
                OurBookDto dto2 = OurBookDto.builder()
                        .bookname(finalBookNameText)
                        .build();
                list2.add(dto2);
            }
        }
        // 여러번 insert 되는 문제를 막기위해 비동기로 진행
        if (!list2.isEmpty()) {
            log.info("insert 시작");
            dao.insert(list2);
            log.info("ourbook에 없는 정보를 insert 하였습니다.");
        } else {
            log.info("추가된 책이 없습니다.");
        }
    }

    //에스 24데이터 가져오는 메소드
    public static List<Yes24Dto> getYes24DataNew(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<Yes24Dto> list = new ArrayList<>();
        int totalpage = stopP;
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page + "&pageSize=120";
            Document doc = Jsoup.connect(pageUrl).get();
            Elements goods = doc.select("[data-goods-no]");

            for (Element good : goods) {
                String gdName = good.select(".gd_name").text();
                String image = good.select(".lazy").attr("data-original");
                Elements yesBs = good.select(".yes_b"); // 가격과 평점 모두 포함된 요소
                String price = yesBs.stream()
                        .filter(e -> e.text().contains("원")) // "원"을 포함하는 텍스트를 가진 요소만 필터링
                        .findFirst() // 첫 번째 요소 선택
                        .map(Element::text) // 텍스트 추출
                        .orElse("가격 정보 없음"); // 가격 정보가 없는 경우
                String infoAuth = good.select(".info_auth").text();
                String infoPub = good.select(".info_pub").text();
                String infoDate = good.select(".info_date").text();
                String rank = good.select(".ico.rank").text();

                Yes24Dto dto = new Yes24Dto(
                        Integer.parseInt(rank),
                        image,
                        gdName,
                        infoAuth,
                        infoPub,
                        price,
                        infoDate
                );
                list.add(dto);
            }
        }
        return list;
    }




}