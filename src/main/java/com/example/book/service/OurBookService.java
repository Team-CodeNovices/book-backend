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

    //베스트,에디터 추천 도서 불러오기
    public List<RecommendBooksDto> randomBooksFromTopN(int start, int end, int pick) throws IOException {
        List<RecommendBooksDto> recommendedBooks = new ArrayList<>();

        List<RankingDto> yes24Top50 = yes24.getYes24Top50();

        if (!yes24Top50.isEmpty()) {

            List<RankingDto> topNBooks = yes24Top50.subList(start, end);
            Collections.shuffle(topNBooks);

            for (int i = 0; i < Math.min(pick, topNBooks.size()); i++) {
                RankingDto rankingDto = topNBooks.get(i);
                RecommendBooksDto recommendBooksDto = new RecommendBooksDto();
                String genre = dao.selectAuthor(rankingDto.getBookname());

                recommendBooksDto.setImage(rankingDto.getImage());
                recommendBooksDto.setGenre(genre);
                recommendBooksDto.setBookname(rankingDto.getBookname());
                recommendBooksDto.setAuthor(rankingDto.getAuthor());

                recommendedBooks.add(recommendBooksDto);
            }
        }

        return recommendedBooks;
    }


    //책에 관련된 추천도서
    public List<RecommendBooksDto> recommendedBooks(String bookname) {
        List<RecommendBooksDto> recommendedBooks = new ArrayList<>();
        Set<String> addedBookNames = new HashSet<>(); // 추천책 중복을 막기위해 사용

        List<OurBookDto> keywordList = dao.selectMainkeyword(bookname);

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
            String author = dao.selectAuthor(bookname);
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