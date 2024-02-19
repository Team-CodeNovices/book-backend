package com.example.book.service;

import com.example.book.dto.AladinDto;
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
@Slf4j
public class AladinService {

    private static final String URL = "https://www.aladin.co.kr/shop/common/wbest.aspx?BranchType=1";


//    //알라딘 정보 가져오기
    public List<AladinDto> getAladinAllData() throws IOException {

        List<AladinDto> bookList = new ArrayList<>();
        int ranking = 0;
        int totalpage=20;

        for(int page=1;page<=totalpage;page++) {
            String pageUrl = URL + "&CID=0&page="+ totalpage + "&cnt=1000&SortOrder=1";
            Document document = Jsoup.connect(pageUrl).get();
            Elements bestSellerBooks = document.getElementsByAttributeValue("class", "ss_book_list");
            Elements bookLinks = bestSellerBooks.select("a.bo3");

            for (Element bookLink : bookLinks) {
                ranking++;
                String href = bookLink.attr("href");
                Document detailDocument = Jsoup.connect(href).get();

                String bookTitle = detailDocument.getElementsByAttributeValue("class", "Ere_bo_title").text();

                AladinDto bookDto = new AladinDto(
                        ranking,
                        bookTitle,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );

                bookList.add(bookDto);
            }
        }
        return bookList;
    }





}
