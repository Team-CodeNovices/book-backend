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
    public static List<Yes24Dto> getYes24DataNew(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<Yes24Dto> list = new ArrayList<>();
        int ranking = 0;
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
                // log.info(gdName + price + infoAuth + infoPub);

                Yes24Dto dto = new Yes24Dto(
                        0,
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

    public static List<Yes24Dto> getYes24Data(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<Yes24Dto> list = new ArrayList<>();
        int ranking = 0;
        int totalpage = stopP; // 999위까지 페이지 갯수

        for (int page = startP; page <= totalpage; page++) {
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
        return list;
    }

    // 제거할 태그의 패턴을 리스트에 저장합니다.
    List<String> removePatterns = Arrays.asList(
            "<iframe.*?>",
            "</?div\\.infoWrap_privew>",
            "<br>", "<br/>",
            "</iframe>",
            "</b>",
            "<B>",
            "<b>"
    );


}
