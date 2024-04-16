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
public class Yes24Service {

    private final RankingWorker asyncService;

    //예스24 크롤링 메소드
    public static List<RankingDto> getYes24DataNew(int startP, int stopP) throws IOException {
        String baseUrl = "https://www.yes24.com/";
        List<RankingDto> list = new ArrayList<>();
        int count = 0;
        int totalpage = stopP;
        for (int page = startP; page <= totalpage; page++) {
            String pageUrl = baseUrl + "/Product/Category/BestSeller?categoryNumber=001&pageNumber=" + page + "&pageSize=120";
            Document doc = Jsoup.connect(pageUrl).get();
            Elements goods = doc.select("[data-goods-no]");

            for (Element good : goods) {
                count++;
                if (count >= 51) {
                    break;
                }
                String gdName = good.select(".gd_name").text();
                if (gdName == null || gdName.isEmpty()) {
                    gdName = "19세 도서";
                }
                String image = good.select(".lazy").attr("data-original");
                String infoAuth = good.select(".info_auth").text();
                String infoPub = good.select(".info_pub").text();
                String rank = good.select(".ico.rank").text();

                RankingDto dto = new RankingDto(
                        Integer.parseInt(rank),
                        image,
                        gdName,
                        infoAuth,
                        infoPub
                );
                list.add(dto);
            }

        }
        return list;
    }

    // yes24 50위 반환하고 전체 크롤링 하는 메소드
    public List<RankingDto> getYes24Top50() throws IOException {
        //50위까지만 처리하고 리턴
        List<RankingDto> yes24Top50 = getYes24DataNew(1, 1);

        // getYes24Another()를 비동기적으로 실행
        asyncService.getYes24Another();
        return yes24Top50;
    }


}
