package com.example.book.service;
import com.example.book.dao.OurBookMapper;
import com.example.book.dto.Yes24Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
@Slf4j
public class Yes24Service {

    private final OurBookMapper dao;
    private final RankingWorker asyncService;

    //예스24 크롤링 메소드
    public static List<Yes24Dto> getYes24DataNew(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<Yes24Dto> list = new ArrayList<>();
        int count = 0;
        int totalpage = stopP; // 999위까지 페이지 갯수
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page + "&pageSize=120";
            Document doc = Jsoup.connect(pageUrl).get();
            Elements goods = doc.select("[data-goods-no]");

            for (Element good : goods) {
                count++;
                if(count >=51){
                    break;
                }
//                String dataGoodsNo = good.attr("data-goods-no");
                String gdName = good.select(".gd_name").text();
                Elements yesBs = good.select(".yes_b"); // 가격과 평점 모두 포함된 요소
                String price = good.select(".yes_b").text().split(" ")[0];
                String infoAuth = good.select(".info_auth").text();
                String infoPub = good.select(".info_pub").text();
                String infoDate = good.select(".info_date").text();
                String rank = good.select(".ico.rank").text();
                //log.info("rank-list : " + rank + gdName + price + infoAuth + infoPub);

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

    // 전체정보 가져오기
    public List<Yes24Dto> getYes24Top50() throws IOException {
        //50위까지만 처리하고 리턴
        List<Yes24Dto> yes24Top50 = getYes24DataNew(1, 1);

        // getYes24Another()를 비동기적으로 실행
        asyncService.getYes24Another();
        return yes24Top50;
    }



    //예스24 크롤링 구버전
//    public List<Yes24Dto> getYes24Data(int startP, int stopP) throws IOException {
//        String baseUrl = "https://www.yes24.com/";
//        List<Yes24Dto> list = new ArrayList<>();
//        int ranking = 0;
//        int totalpage = stopP; // 999위까지 페이지 갯수
//
//        for (int page = startP; page <= totalpage; page++) {
//            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page;
//            Document document = Jsoup.connect(pageUrl).get();
//            Elements yes24 = document.getElementsByAttributeValue("class", "img_grp");
//            Elements yes24url = yes24.select("a[href]");
//
//            List<CompletableFuture<Void>> futures = new ArrayList<>();
//
//            for (Element a : yes24url) {
//                ranking++;
//                String href = baseUrl + a.attr("href");
//                int finalRanking = ranking;
//                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                    try {
//                        Document detailurl = Jsoup.connect(href).get();
//                        Elements Bookname = detailurl.getElementsByAttributeValue("class", "gd_titArea");
//                        Elements bookname = Bookname.select(".gd_name");
//                        Elements author = detailurl.getElementsByAttributeValue("class", "gd_auth");
//                        Elements publisher = detailurl.getElementsByAttributeValue("class", "gd_pub");
//                        Element priceClass = detailurl.getElementsByAttributeValue("class", "yes_m").first();
//
//
//                        // bookname이 null이면 19세 도서로 처리합니다.
//                        String bookNameText = bookname.text();
//                        if (bookNameText == null || bookNameText.isEmpty()) {
//                            bookNameText = "19세 도서";
//                        }
//
//                        String price;
//                        // 해당 요소가 null인지 확인하여 예외 처리합니다.
//                        if (priceClass != null) {
//                            String pricerm = priceClass.text();
//                            price = pricerm.replaceFirst("원", "");
//                        } else {
//                            price = "가격 정보 없음";
//                        }
//
//                        Elements writerdate = detailurl.getElementsByAttributeValue("class", "gd_date");
//
//                        Yes24Dto dto = new Yes24Dto(
//                                finalRanking,
//                                bookNameText,
//                                author.text(),
//                                publisher.text(),
//                                price,
//                                writerdate.text()
//                        );
//                        log.info("예스24 " + finalRanking + "위 정보를 불러옵니다.");
//                        list.add(dto);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//                futures.add(future);
//            }
//
//            // CompletableFuture 모두 완료될 때까지 대기
//            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//            allOf.join();
//        }
//        return list;
//    }


}
