package com.example.book.service;

import com.example.book.dao.BookReportMapper;
import com.example.book.dto.BookReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReportService {

    private final BookReportMapper dao;

    public void reportInsert(BookReportDto dto) {
        dao.reportInsert(dto);
    }

    public List<BookReportDto> reportall(){
       return dao.reportall();
    }

    public BookReportDto reportselect(BookReportDto dto){
        return dao.reportselect(dto);

    }
    public int  reportupdate(BookReportDto dto){
        return dao.reportupdate(dto);
    }

    public void reportdelete(BookReportDto dto){
        dao.reportdelete(dto);
    }
}
