package com.example.book.service;


import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OurBookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookDetailInfo {
    private final OurBookMapper book;


    public List<OurBookDto> Bookdetailinfo(String bookName) {
        return book.bookdetailinfo(bookName);
    }


}
