package com.example.book.service;

import com.example.book.dto.Yes24Dto;
import lombok.RequiredArgsConstructor;
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
public class Yes24Service {


//    //예스 24 랭킹 50위
//    public List<Yes24Dto> getYes24Data() throws IOException {
//
//        String baseUrl = "https://www.yes24.com/";
//        List<Yes24Dto> list = new ArrayList<>();
//        int ranking = 0;
//        int totalpage =3;       //50위까지 페이지 갯수
//
//        // 책 목록 페이지에 접근
//        for (int page = 1; page <= totalpage; page++) {
//            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page;
//            Document document = Jsoup.connect(pageUrl).get();
//            Elements yes24 = document.getElementsByAttributeValue("class", "img_grp");
//            Elements yes24url = yes24.select("a[href]");
//        // 각 책의 상세 페이지에 접근하여 정보 추출
//        for (Element a : yes24url) {
//            String href = baseUrl + a.attr("href");
//            Document detailurl = Jsoup.connect(href).get();
//            Elements Bookname = detailurl.getElementsByAttributeValue("class", "gd_titArea");
//            Elements bookname = Bookname.select(".gd_name");
//            Elements author = detailurl.getElementsByAttributeValue("class", "gd_auth");
//            Elements publisher = detailurl.getElementsByAttributeValue("class", "gd_pub");
//            Elements writerdate = detailurl.getElementsByAttributeValue("class", "gd_date");
//            Elements price = detailurl.getElementsByAttributeValue("class", "yes_m");
//            ranking++;
//
//            Yes24Dto dto = Yes24Dto.builder()
//                    .ranking(ranking)
//                    .bookname(bookname.text())
//                    .author(author.text())
//                    .publisher(publisher.text())
////                    .price(Integer.parseInt(price.text()))
//                    .writedate(writerdate.text())
//                    .build();
//            list.add(dto);
//
//            if (ranking >= 50) {
//                break;
//            }
//        }
//        }
//        return list;
//    }


    //예스 24 랭킹 50위
    public List<Yes24Dto> getYes24Data() throws IOException {

        String baseUrl = "https://www.yes24.com/";
        List<Yes24Dto> list = new ArrayList<>();
        int ranking = 0;
        int totalpage =3;       //50위까지 페이지 갯수

        // 책 목록 페이지에 접근
        for (int page = 1; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page;
            Document document = Jsoup.connect(pageUrl).get();
            Elements yes24 = document.getElementsByAttributeValue("class", "img_grp");
            Elements yes24url = yes24.select("a[href]");
        // 각 책의 상세 페이지에 접근하여 정보 추출
        for (Element a : yes24url) {
            String href = baseUrl + a.attr("href");
            Document detailurl = Jsoup.connect(href).get();
            Elements Bookname = detailurl.getElementsByAttributeValue("class", "gd_titArea");
            Elements bookname = Bookname.select(".gd_name");
            Elements author = detailurl.getElementsByAttributeValue("class", "gd_auth");
            Elements publisher = detailurl.getElementsByAttributeValue("class", "gd_pub");
            Elements writerdate = detailurl.getElementsByAttributeValue("class", "gd_date");
//            Elements price = detailurl.getElementsByAttributeValue("class", "yes_m");
            ranking++;

            Yes24Dto dto = new Yes24Dto(
                    ranking,
                    bookname.text(),
                    author.text(),
                    publisher.text(),
                    null,
                    null,
                    0,
                    writerdate.text()
            );

            list.add(dto);

            if (ranking >= 50) {
                break;
            }
        }
        }
        return list;
    }

    //전체정보 가져오기
    public List<Yes24Dto> getYes24AllData() throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<Yes24Dto> list = new ArrayList<>();
        int ranking = 0;
        int totalpage =42;       //50위까지 페이지 갯수
        for (int page = 1; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page;
            Document document = Jsoup.connect(pageUrl).get();
            Elements yes24 = document.getElementsByAttributeValue("class", "img_grp");
            Elements yes24url = yes24.select("a[href]");
            for (Element a : yes24url) {
                ranking++;
                String href = baseUrl + a.attr("href");
                Document detailurl = Jsoup.connect(href).get();
                Elements Bookname = detailurl.getElementsByAttributeValue("class", "gd_titArea");
                Elements bookname = Bookname.select(".gd_name");
//                Elements author = detailurl.getElementsByAttributeValue("class", "gd_auth");
//                Elements publisher = detailurl.getElementsByAttributeValue("class", "gd_pub");
//                Elements writerdate = detailurl.getElementsByAttributeValue("class", "gd_date");
//                Elements price = detailurl.getElementsByAttributeValue("class", "yes_m");
                ranking++;

                Yes24Dto dto = new Yes24Dto(
                        ranking,
                        bookname.text(),
                        null,
                        null,
                        null,
                        null,
                        0,
                        null
                );

                list.add(dto);
            }
        }
        return list;
    }



}
