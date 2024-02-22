package com.example.book.service;

import com.example.book.dao.OurBookMapper;
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
    
    //에스 24데이터 가져오는 메소드
    public static List<Yes24Dto> getYes24DataNew(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<Yes24Dto> list = new ArrayList<>();
        int totalpage = stopP; // 999위까지 페이지 갯수
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page + "&pageSize=120";
            Document doc = Jsoup.connect(pageUrl).get();
            Elements goods = doc.select("[data-goods-no]");

            for (Element good : goods) {
//                String dataGoodsNo = good.attr("data-goods-no");
                String gdName = good.select(".gd_name").text();
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
                // log.info(gdName + price + infoAuth + infoPub);

                Yes24Dto dto = new Yes24Dto(
                        Integer.parseInt(rank),
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
