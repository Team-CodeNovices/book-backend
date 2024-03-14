package com.example.book.service;


import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OurBookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookDetailInfo {
    private final OurBookMapper book;


    public List<OurBookDto> Bookdetailinfo(String bookname) {
        if (bookname.length() < 2) {
            // 키워드가 두 글자 미만이면 빈 리스트 반환
            return new ArrayList<>();
        } else {
            // 두 글자 이상의 키워드가 포함된 책 정보 반환
            return book.bookdetailinfo(bookname);
        }
    }
}


