package com.example.book.dao;

import com.example.book.dto.OurBookDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OurBookMapper {

    //insert
    void insert(List<OurBookDto> dto);

    //ourbook 모든 데이터 보기
    List<OurBookDto> select();

    //detail null 인 목록 보기
    List<OurBookDto> selectnull();

    //keyword null 인 목록 보기
    List<OurBookDto> keywordnull();

    //업데이트 스케줄러
    void updateBooksByList(List<OurBookDto> nullList);

    //메인키워드 업데이트
    void updateMainKeyword(List<OurBookDto> nullList);

    //키워드 검색
    List<OurBookDto> searchKeyword(@Param("keyword") String keyword);

    List<OurBookDto> bookdetailinfo(String bookname);


}
