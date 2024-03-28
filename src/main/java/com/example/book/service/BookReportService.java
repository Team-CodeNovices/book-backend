package com.example.book.service;

import com.example.book.dao.BookReport;
import com.example.book.dto.BookReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReportService {

    private final BookReport dao;

    public void reportInsert(BookReportDto dto) {
        dao.reportInsert(dto);
    }





}
