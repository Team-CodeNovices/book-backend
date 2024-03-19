package com.example.book.service;

import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OurBookDto;
import com.example.book.dto.RankingDto;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankingWorker {

    private final OurBookMapper dao;

    //알라딘 전체데이터 가져온 후 insert 하는 메소드(비동기)
    @Async
    public void getAladinAnother() throws IOException {
        List<RankingDto> list = getAladinData(1, 20);
        insertAladinData(list);
    }

    //알라딘 크롤링 하는 메소드
    public static List<RankingDto> getAladinData(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.aladin.co.kr/";
        List<RankingDto> list = new ArrayList<>();
        int totalpage = stopP; // 999위까지 페이지 갯수
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/shop/common/wbest.aspx?BestType=Bestseller&BranchType=1&CID=0&cnt=1000&SortOrder=1&page=" + page;
            Document doc = Jsoup.connect(pageUrl).get();

            Elements bookBoxes = doc.select(".ss_book_box");
            for (Element bookBox : bookBoxes) {
                String bookTitle = bookBox.select(".bo3").text().split("\\(")[0].trim();

                RankingDto dto = RankingDto.rankingDtoBuilder().bookname(bookTitle).build();
                list.add(dto);
            }
        }
        return list;
    }

    //ourbook에 없는 데이터 insert 하는 메소드(알라딘)
    public void insertAladinData(List<RankingDto> aladin) throws IOException {
        List<OurBookDto> existBooks = dao.select();
        List<OurBookDto> list2 = new ArrayList<>();

        for (RankingDto aladinDto : aladin) {

            String finalBookNameText = aladinDto.getBookname();
            boolean exist = existBooks.stream().anyMatch(existingBook -> existingBook.getBookname().replaceAll("\\s", "").equals(finalBookNameText.replaceAll("\\s", "")));
            if (!exist) {
                OurBookDto dto2 = OurBookDto.ourBookDtoBuilder().bookname(finalBookNameText).build();
                list2.add(dto2);
            }
        }
        if (!list2.isEmpty()) {
            log.info("insert 시작 (알라딘)");
            dao.insert(list2);
            log.info("새로운 알라딘 책 정보를 insert 하였습니다.");
        } else {
            log.info("추가된 책이 없습니다.");
        }
    }

    //yes24 전체데이터 가져온 후 insert 하는 메소드(비동기)
    @Async
    public void getYes24Another() throws IOException {
        List<RankingDto> list = getYes24DataNew(1, 9);
        insertYes24Data(list);
    }

    //에스 24 크롤링 하는 메소드
    public static List<RankingDto> getYes24DataNew(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<RankingDto> list = new ArrayList<>();
        int totalpage = stopP;
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page + "&pageSize=120";
            Document doc = Jsoup.connect(pageUrl).get();
            Elements goods = doc.select("[data-goods-no]");

            for (Element good : goods) {
                String gdName = good.select(".gd_name").text().split("\\(")[0].trim();
                if (gdName == null || gdName.isEmpty()) {
                    gdName = "19세 도서";
                }

                RankingDto dto = RankingDto.rankingDtoBuilder().bookname(gdName).build();
                list.add(dto);
            }
        }
        return list;
    }

    //ourbook에 없는 데이터 insert 하는 메소드(yes24)
    public void insertYes24Data(List<RankingDto> yes24) throws IOException {
        List<OurBookDto> existBooks = dao.select();
        List<OurBookDto> list2 = new ArrayList<>();

        for (RankingDto yes24Dto : yes24) {

            String finalBookNameText = yes24Dto.getBookname();
            boolean exist = existBooks.stream().anyMatch(existingBook -> existingBook.getBookname().replaceAll("\\s", "").equals(finalBookNameText.replaceAll("\\s", "")));
            if (!exist) {
                OurBookDto dto2 = OurBookDto.ourBookDtoBuilder().bookname(finalBookNameText).build();
                list2.add(dto2);
            }
        }
        // 여러번 insert 되는 문제를 막기위해 비동기로 진행
        if (!list2.isEmpty()) {
            log.info("insert 시작 (Yes24)");
            dao.insert(list2);
            log.info("새로운 Yes24 책 정보를 insert 하였습니다.");
        } else {
            log.info("추가된 책이 없습니다.");
        }
    }


}