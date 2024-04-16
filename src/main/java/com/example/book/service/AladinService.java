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
public class AladinService {

    private final RankingWorker asyncService;

    private static final String baseUrl = "https://www.aladin.co.kr/";

    //알라딘 크롤링 메소드
    public static List<RankingDto> getAladinData(int startP, int stopP) throws IOException {
        List<RankingDto> list = new ArrayList<>();
        int ranking = 1;
        int totalpage = stopP;
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/shop/common/wbest.aspx?BestType=Bestseller&BranchType=1&CID=0&cnt=1000&SortOrder=1&page=" + page;
            Document doc = Jsoup.connect(pageUrl).get();

            Elements bookBoxes = doc.select(".ss_book_box");
            for (Element bookBox : bookBoxes) {

                String bookTitle = bookBox.select(".bo3").text();
                String image = bookBox.select(".front_cover").attr("src");

                Element infoElement = bookBox.selectFirst(".bo3").parent().nextElementSibling();
                String authorAndPublisher = infoElement.text();

                String[] parts = authorAndPublisher.split("\\|");
                String author = parts.length > 0 ? parts[0].trim() : "";
                String publisher = parts.length > 1 ? parts[1].trim() : "";

                RankingDto dto = new RankingDto(
                        ranking,
                        image,
                        bookTitle,
                        author,
                        publisher
                );
                list.add(dto);
                ranking++;
            }
        }
        return list;
    }

    //알라딘 50위 반환하고 전체 크롤링 하는 메소드
    public List<RankingDto> getAladinTop50() throws IOException {
        List<RankingDto> AladinTop50 = getAladinData(1, 1);

        asyncService.getAladinAnother();

        return AladinTop50;
    }

}
