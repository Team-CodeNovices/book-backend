package com.example.book.service;

import com.example.book.dto.YPBookDto;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YPBookService {

    public List<YPBookDto> list() throws IOException {
        Document doc = null;
        String baseUrl = "https://www.ypbooks.co.kr";
        doc = Jsoup.connect("https://www.ypbooks.co.kr/book_arrange.yp?targetpage=book_week_best&pagetype=5&depth=1").get();
        Elements contentslink = doc.getElementsByAttributeValue("class", "boxiconset_bk_tt");
        Elements links = contentslink.select("a[href]");
        List<YPBookDto> list = new ArrayList<>();

        for (Element link : links) {
            // a 태그의 href 속성 가져오기
            String hrefValue = link.attr("href");
            // 가져온 href 출력
            String fullUrl = baseUrl + hrefValue;
            Document innerDocument = Jsoup.connect(fullUrl).get();
            Elements titleClass = innerDocument.getElementsByClass("sm-title-in");
            Elements authorClass = innerDocument.getElementsByAttributeValue("class", "author");
            Elements publisherClass = innerDocument.getElementsByAttributeValue("class", "publisher");
            Elements publicationDateClass = innerDocument.getElementsByAttributeValue("class", "publication-date");
            String titleTag = titleClass.select("h3").text();
            String author = authorClass.text();
            String publisher = publisherClass.text();
            String publicationDate = publicationDateClass.text();
            String title = titleTag.replaceFirst("-.*", "");

            // div 태그 선택
            Elements divs = innerDocument.select("div.introduce");
            String bText = null;
            String nextText = null;
            // div 태그 내부의 b 태그와 br 태그 출력
            // div 태그 내부의 b 태그와 br 태그 출력
            for (Element bTag : divs) {
                bText = bTag.text();
                System.out.println("Text inside <b>: " + bText);
            }

            // div 태그 내부의 br 태그 출력
            Elements brTags = divs.select("br");
            for (Element brTag : brTags) {
                Node sibling = brTag.nextSibling(); // 다음 형제 노드 가져오기
                if (sibling != null && sibling instanceof TextNode) { // 형제 노드가 텍스트 노드인지 확인
                    nextText = ((TextNode) sibling).text().trim(); // 형제 노드가 텍스트 노드인 경우 텍스트 가져오기
                    System.out.println("Text after <br>: " + nextText);
                }
            }

            YPBookDto dto = YPBookDto.builder()
                    .bookname(title)
                    .author(author)
                    .publisher(publisher)
                    .bookInfo(bText + nextText)
                    .publicationdate(publicationDate)
                    .build();
            list.add(dto);
        }
        return list;
    }
}
