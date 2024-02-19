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
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class Yes24Service {

    private final OurBookMapper dao;


    // 전체정보 가져오기
    public List<Yes24Dto> getYes24AllData() throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<Yes24Dto> list = new ArrayList<>();
        List<OurBookDto> list2 = new ArrayList<>();
        List<OurBookDto> existBooks = dao.select();
        int ranking = 0;
        int totalpage = 9; // 999위까지 페이지 갯수

        for (int page = 1; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page + "&pageSize=120";
            Document document = Jsoup.connect(pageUrl).get();
            Elements yes24 = document.getElementsByAttributeValue("class", "img_grp");
            Elements yes24url = yes24.select("a[href]");

            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (Element a : yes24url) {
                ranking++;
                String href = baseUrl + a.attr("href");
                int finalRanking = ranking;
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        Document detailurl = Jsoup.connect(href).get();
                        Elements Bookname = detailurl.getElementsByAttributeValue("class", "gd_titArea");
                        Elements bookname = Bookname.select(".gd_name");
                        Elements author = detailurl.getElementsByAttributeValue("class", "gd_auth");
                        Elements publisher = detailurl.getElementsByAttributeValue("class", "gd_pub");
                        Element priceClass = detailurl.getElementsByAttributeValue("class", "yes_m").first();


                        // bookname이 null이면 19세 도서로 처리합니다.
                        String bookNameText = bookname.text();
                        if (bookNameText == null || bookNameText.isEmpty()) {
                            bookNameText = "19세 도서";
                        }

                        String price;
                        // 해당 요소가 null인지 확인하여 예외 처리합니다.
                        if (priceClass != null) {
                            String pricerm = priceClass.text();
                            price = pricerm.replaceFirst("원", "");
                        } else {
                            price = "가격 정보 없음";
                        }

                        Elements writerdate = detailurl.getElementsByAttributeValue("class", "gd_date");
                        if (finalRanking <= 50) {
                            Yes24Dto dto = new Yes24Dto(
                                    finalRanking,
                                    bookNameText,
                                    author.text(),
                                    publisher.text(),
                                    price,
                                    writerdate.text()
                            );
                            log.info("예스24 " + finalRanking + "위 정보를 불러옵니다.");
                            list.add(dto);
                        }

                        // 전체 도서 중에서 이미 존재하는 도서는 list2에 추가
                        String finalBookNameText = bookNameText;
                        boolean exist = existBooks.stream().anyMatch(existingBook -> existingBook.getBookname().equals(finalBookNameText));
                        if (!exist) {
                            OurBookDto dto2 = OurBookDto.builder()
                                    .bookname(bookNameText)
                                    .build();
                            list2.add(dto2);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                futures.add(future);
            }

            // CompletableFuture 모두 완료될 때까지 대기
            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allOf.join();
        }

        // 여러번 insert 되는 문제를 막기위해 비동기로 진행
        if (!list2.isEmpty()) {
            dao.insert(list2);
            log.info("ourbook에 없는 정보를 insert 하였습니다.");
        } else {
            log.info("추가된 책이 없습니다.");
        }

        return list;
    }






}
