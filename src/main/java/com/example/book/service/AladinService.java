package com.example.book.service;

import com.example.book.dto.AladinDto;
import com.example.book.dto.Yes24Dto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Slf4j
public class AladinService {
    private final RankingWorker asyncService;
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
                String price = bookBox.select(".ss_p2").text().replace("원", "").trim(); // 가격 추출, "원" 제거

                // 작가, 출판사, 출판일자 정보가 포함된 요소를 찾습니다.
                // 이 정보는 페이지의 구조에 따라 다를 수 있으므로, 예제에서는 간단화를 위해 직접적인 선택자를 사용합니다.
                // 실제 구조에 맞게 선택자를 조정해야 할 수 있습니다.
                Element infoElement = bookBox.selectFirst(".bo3").parent().nextElementSibling(); // 가정: 작가 및 출판사 정보가 bo3의 부모 요소의 다음 형제 요소에 위치
                String authorAndPublisher = infoElement.text(); // 작가 및 출판사 정보 추출

                // 작가 및 출판사 정보 분리 (실제 페이지 구조에 따라 다를 수 있음)
                String[] parts = authorAndPublisher.split("\\|"); // "|"를 구분자로 사용하여 분리
                String author = parts.length > 0 ? parts[0].trim() : ""; // 첫 번째 부분을 작가 정보로 사용
                String publisher = parts.length > 1 ? parts[1].trim() : ""; // 두 번째 부분을 출판사 정보로 사용
                String publishDate = parts.length > 2 ? parts[2].trim() : ""; // 세 번째 부분을 출판일자로 사용
//                log.info("rank-list : " + bookTitle + price + author + publisher + publishDate);

                AladinDto dto = new AladinDto(
                        ranking,
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
    private static final String URL = "https://www.aladin.co.kr/shop/common/wbest.aspx?BranchType=1";


    public List<AladinDto> getAladinTop50() throws IOException {
        //50위까지만 처리하고 리턴
        List<AladinDto> AladinTop50 = getAladinData(1, 1);

        // getYes24Another()를 비동기적으로 실행
        asyncService.getAladinAnother();

        return AladinTop50;
    }
//
////    //알라딘 정보 가져오기
//    public List<AladinDto> getAladinAllData() throws IOException {
//
//        List<AladinDto> bookList = new ArrayList<>();
//        int ranking = 0;
//        int totalpage=20;
//
//        for(int page=1;page<=totalpage;page++) {
//            String pageUrl = URL + "&CID=0&page="+ totalpage + "&cnt=1000&SortOrder=1";
//            Document document = Jsoup.connect(pageUrl).get();
//            Elements bestSellerBooks = document.getElementsByAttributeValue("class", "ss_book_list");
//            Elements bookLinks = bestSellerBooks.select("a.bo3");
//
//            for (Element bookLink : bookLinks) {
//                ranking++;
//                String href = bookLink.attr("href");
//                Document detailDocument = Jsoup.connect(href).get();
//
//                String bookTitle = detailDocument.getElementsByAttributeValue("class", "Ere_bo_title").text();
//
//                AladinDto bookDto = new AladinDto(
//                        ranking,
//                        bookTitle,
//                        null,
//                        null,
//                        null,
//                        null,
//                        null,
//                        null
//                );
//
//                bookList.add(bookDto);
//            }
//        }
//        return bookList;
//    }





}
