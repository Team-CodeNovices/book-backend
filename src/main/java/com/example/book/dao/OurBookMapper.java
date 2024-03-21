package com.example.book.dao;

import com.example.book.dto.OurBookDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OurBookMapper {

    //insert
    void insert(List<OurBookDto> dto);

    //ourbook 모든 데이터 보기
    List<OurBookDto> select();

    //카테고리에 따른 정보
    List<OurBookDto> selectCategory(Map<String, Object> params);

    //매인키워드 데이터 보기
    List<OurBookDto> selectMainkeyword(String bookname);

    //키워드가 포함된 책리스트
    List<OurBookDto> containKeyword(String keyword);

    //책의 저자 가져오기
    String selectAuthor(String bookname);
    
    //비어있는 목록 가져오기
    List<OurBookDto> selectnull();

    //api 업데이트 스케줄러
    void updateBooksByList(List<OurBookDto> nullList);

    //메인키워드 업데이트 스케줄러
    void updateMainKeyword(List<OurBookDto> nullList);

    //키워드 검색
    List<OurBookDto> searchKeyword(@Param("keyword") String keyword);

    List<OurBookDto> bookdetailinfo(String bookname);

    List<OurBookDto> authorinfo(String author);

    List<OurBookDto> publisherinfo(String publisher);


}
