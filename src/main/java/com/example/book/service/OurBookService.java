package com.example.book.service;


import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OurBookDto;
import com.example.book.dto.RankingDto;
import com.example.book.dto.RecommendBooksDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OurBookService {

    private final OurBookMapper dao;
    private final Yes24Service yes24;
    private final AladinService aladin;
    private final YPBookService yp;


    //ourbook 리스트 불러오기
    public List<OurBookDto> selectlist() {
        return dao.select();
    }

    //키워드 검색
    public List<OurBookDto> searchByKeyword(String keyword) throws IOException {
        keyword = keyword.replace("\\s", "");
        if (keyword.length() < 2) {
            return new ArrayList<>();
        }
        return dao.searchKeyword(keyword);
    }

    //추천 도서 불러오기
    public List<RecommendBooksDto> getRandomBooks() throws IOException {
        Random random = new Random(System.currentTimeMillis());
        int randomSite = random.nextInt(2);
        List<RecommendBooksDto> recommendedBooks = null;

        switch (randomSite) {
            case 0:
                List<RankingDto> aladinTop50 = aladin.getAladinTop50();
                if (!aladinTop50.isEmpty()) {
                    recommendedBooks = getRecommendedBooksFromSite(aladinTop50.get(0).getBookname());
                }
                break;
            case 1:
                List<RankingDto> yes24Top50 = yes24.getYes24Top50();
                if (!yes24Top50.isEmpty()) {
                    recommendedBooks = getRecommendedBooksFromSite(yes24Top50.get(0).getBookname());
                }
                break;
            case 2:
                List<RankingDto> ypTop20 = yp.list();
                if (!ypTop20.isEmpty()) {
                    recommendedBooks = getRecommendedBooksFromSite(ypTop20.get(0).getBookname());
                }
                break;
            default:
                recommendedBooks = new ArrayList<>(); // 기본적으로 빈 리스트 반환 또는 오류 처리
                break;
        }
        return recommendedBooks;
    }


    //랜덤 사이트 랭킹 1위 책관련 도서 추천 메소드
    private List<RecommendBooksDto> getRecommendedBooksFromSite(String topBookName) {
        List<RecommendBooksDto> recommendedBooks = new ArrayList<>();
        Set<String> addedBookNames = new HashSet<>(); // 추천책 중복을 막기위해 사용

        List<OurBookDto> keywordList = dao.selectMainkeyword(topBookName);

        if (keywordList != null && !keywordList.isEmpty()) {
            List<String> keywords = new ArrayList<>();
            for (OurBookDto mainKeyword : keywordList) {
                String keyword = mainKeyword.getMainkeyword();
                String[] keywordParts = keyword.split("\\d+\\.\\s*,\\s*");
                for (String part : keywordParts) {
                    if (!part.trim().isEmpty()) {
                        keywords.add(part.trim());
                    }
                }
            }

            for (String keyword : keywords) {
                List<OurBookDto> booksWithKeyword = dao.containKeyword(keyword);
                for (OurBookDto randomBook : booksWithKeyword) {
                    if (!addedBookNames.contains(randomBook.getBookname()) && recommendedBooks.size() < 5) {
                        RecommendBooksDto recommendBooksDto = new RecommendBooksDto();
                        recommendBooksDto.setImage(randomBook.getImage());
                        recommendBooksDto.setGenre(randomBook.getGenre());
                        recommendBooksDto.setBookname(randomBook.getBookname());
                        recommendBooksDto.setAuthor(randomBook.getAuthor());
                        recommendedBooks.add(recommendBooksDto);
                        addedBookNames.add(randomBook.getBookname());
                    }
                }
            }

        } else {    //정보가 비어있을 시 그책 저자의 관련 책들 추천
            String author = dao.selectAuthor(topBookName);
            Map<String, Object> params = new HashMap<>();
            params.put("author", author);
            List<OurBookDto> randomBooks = dao.selectCategory(params);
            for (OurBookDto randomBook : randomBooks) {
                if (!addedBookNames.contains(randomBook.getBookname()) && recommendedBooks.size() < 5) {
                    RecommendBooksDto recommendBooksDto = new RecommendBooksDto();
                    recommendBooksDto.setImage(randomBook.getImage());
                    recommendBooksDto.setGenre(randomBook.getGenre());
                    recommendBooksDto.setBookname(randomBook.getBookname());
                    recommendBooksDto.setAuthor(randomBook.getAuthor());
                    recommendedBooks.add(recommendBooksDto);
                    addedBookNames.add(randomBook.getBookname());
                }
            }
        }
        return recommendedBooks;
    }


}