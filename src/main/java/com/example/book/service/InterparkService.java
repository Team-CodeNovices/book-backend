package com.example.book.service;

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
public class InterparkService {

    //영품문고 책정보 리스트 불러오고 이름 비교해서 OurBook테이블에 넣는 메소드
    public List<RankingDto> list() throws IOException {
        List<RankingDto> list = new ArrayList<>();

        String baseUrl = "https://book.interpark.com";
        Document doc = Jsoup.connect(baseUrl + "/display/collectlist.do?_method=BestsellerHourNew201605&bestTp=1&dispNo=").get();
        Elements contents = doc.select("div.listItem.singleType"); // class명에 대한 선택자 수정

        int ranking = 0;

        for (Element content : contents) {
            String booktitle = content.select(".itemName").text(); // 책 제목 가져오기
            String author = content.select("span.author").text(); // 저자 가져오기
            String publisher = content.select("span.company").text(); // 출판사 가져오기
            String image = content.select("img[src]").attr("src"); // 이미지 URL 가져오기

            ranking++;

            RankingDto dto = new RankingDto(ranking, image, booktitle, author, publisher);
            list.add(dto);

        }

        return list;
    }
}