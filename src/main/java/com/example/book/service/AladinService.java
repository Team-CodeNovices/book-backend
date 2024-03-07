package com.example.book.service;

import com.example.book.dto.AladinDto;

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
    public static List<AladinDto> getAladinData(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.aladin.co.kr/";
        List<AladinDto> list = new ArrayList<>();
        int ranking = 1;
        int totalpage = stopP;
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/shop/common/wbest.aspx?BestType=Bestseller&BranchType=1&CID=0&cnt=1000&SortOrder=1&page=" + page;
            Document doc = Jsoup.connect(pageUrl).get();

            Elements bookBoxes = doc.select(".ss_book_box");
            for (Element bookBox : bookBoxes) {

                String bookTitle = bookBox.select(".bo3").text();
                String price = bookBox.select(".ss_p2").text().replace("원", "").trim();
                String image = bookBox.select(".front_cover").attr("src");

                Element infoElement = bookBox.selectFirst(".bo3").parent().nextElementSibling();
                String authorAndPublisher = infoElement.text();


                String[] parts = authorAndPublisher.split("\\|");
                String author = parts.length > 0 ? parts[0].trim() : "";
                String publisher = parts.length > 1 ? parts[1].trim() : "";
                String publishDate = parts.length > 2 ? parts[2].trim() : "";


                AladinDto dto = new AladinDto(
                        ranking,
                        image,
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
        List<AladinDto> AladinTop50 = getAladinData(1, 1);

        asyncService.getAladinAnother();

        return AladinTop50;
    }

}
