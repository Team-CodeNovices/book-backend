package com.example.book.service;

import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OurBookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {
    private final OurBookMapper book;

    // 파라미터 순서를 변경하여 검색 메서드 수정

    //public List<OurBookDto> searchBooks(String searchType, String keyword) {
        //return book.searchBooks(searchType, keyword);
   // }
    public List<OurBookDto> getAllbooks() {
        return book.select();
    }
}



