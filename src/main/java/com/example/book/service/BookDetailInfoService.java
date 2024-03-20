package com.example.book.service;


import com.example.book.dao.OurBookMapper;
import com.example.book.dto.OurBookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookDetailInfoService {
    private final OurBookMapper book;


    public List<OurBookDto> Bookdetailinfo(String bookname) throws IOException {
            return book.bookdetailinfo(bookname);

    }
    public List<OurBookDto>  authorinfo(String author) throws IOException {
        return book.authorinfo(author);
    }
    public List<OurBookDto> publisherinfo(String publisher) throws IOException{
        return book.publisherinfo(publisher);
    }
}


