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
public class SearchService {
    private final OurBookMapper book;


    public List<OurBookDto> searchByPublisher(String publisher) {
        return book.searchByPublisher(publisher);
    }

    public List<OurBookDto> searchByAuthor(String author) {

        return book.searchByAuthor(author);
    }


}




