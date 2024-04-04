package com.example.book.dao;

import com.example.book.dto.BookReportDto;
import com.example.book.dto.ReportLikeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookReportMapper {

    //게시물 등록
    void reportInsert(BookReportDto dto);

    //게시물 전체보기
    List<BookReportDto> reportall();

    BookReportDto reportselect(BookReportDto dto);

    //게시물 수정
    int reportupdate(BookReportDto dto);
    //게시물 삭제
    void reportdelete(BookReportDto dto);

    void liketrue(ReportLikeDto dto);

    void likefalse(ReportLikeDto dto);
}
