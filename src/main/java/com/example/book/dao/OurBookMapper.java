package com.example.book.dao;

import com.example.book.dto.OurBookDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OurBookMapper {

    //insert
    void insert(List<OurBookDto> dto);


    //ourbook 모든 데이터 보기
    List<OurBookDto> select();
    
    //detail null 인 목록 보기
    List<OurBookDto> selectnull();

    //업데이트 스케줄러
    void updateBooksByList(List<OurBookDto> nullList);



}
