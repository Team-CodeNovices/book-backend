package com.example.book.dao;

import com.example.book.dto.BookReportDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookReport {

    //게시물 등록
    BookReportDto reportInsert(BookReportDto dto);

}
