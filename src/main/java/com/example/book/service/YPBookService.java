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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class YPBookService {

    private final OurBookMapper dao;

    //영품문고 책정보 리스트 불러오고 이름 비교해서 OurBook테이블에 넣는 메소드
    public List<RankingDto> list() throws IOException {
        List<OurBookDto> existBooks = dao.select();
        List<OurBookDto> list2 = new ArrayList<>();
        List<RankingDto> list = new ArrayList<>();

        Document doc = null;
        String baseUrl = "https://www.ypbooks.co.kr";
        doc = Jsoup.connect("https://www.ypbooks.co.kr/book_arrange.yp?targetpage=book_week_best&pagetype=5&depth=1").get();
        Elements contentslink = doc.getElementsByAttributeValue("class", "boxiconset_bk_tt");
        Elements links = contentslink.select("a[href]");
        int ranking = 0;

        for (Element link : links) {
            String hrefValue = link.attr("href");
            String fullUrl = baseUrl + hrefValue;
            Document innerDocument = Jsoup.connect(fullUrl).get();
            Elements titleClass = innerDocument.getElementsByClass("sm-title-in");
            Elements authorClass = innerDocument.getElementsByAttributeValue("class", "author");
            Elements publisherClass = innerDocument.getElementsByAttributeValue("class", "publisher");
            Elements publicationDateClass = innerDocument.getElementsByAttributeValue("class", "publication-date");
            Element imageClass = innerDocument.select("div.sm-book span.cover img").get(0);
            String titleTag = titleClass.select("h3").text();
            String author = authorClass.text();
            String publisher = publisherClass.text();
            String image = imageClass.attr("src");
            String publicationDate = publicationDateClass.text();
            String title = titleTag.replaceFirst("-.*", "").trim();
            title = title.split("\\(")[0].trim();
            ranking++;

            RankingDto dto = new RankingDto(
                    ranking,
                    image,
                    title,
                    author,
                    publisher,
                    publicationDate
            );
            list.add(dto);
        }

        for (RankingDto ypDto : list) {
            String finalBookNameText = ypDto.getBookname();
            boolean exist = existBooks.stream().anyMatch(existingBook -> existingBook.getBookname().equals(finalBookNameText));
            if (!exist) {
                OurBookDto dto2 = OurBookDto.ourBookDtoBuilder()
                        .bookname(finalBookNameText)
                        .build();
                list2.add(dto2);
            }
        }
        if (!list2.isEmpty()) {
            log.info("insert 시작 (영풍문고)");
            dao.insert(list2);
            log.info("새로운 영풍문고 책 정보를 insert 하였습니다.");
        } else {
            log.info("추가된 책이 없습니다.");
        }
        return list;
    }
}