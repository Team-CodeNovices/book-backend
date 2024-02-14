package com.example.book.dao;

import com.example.book.dto.OurBookDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OurBookMapper {

    //insert
    void insert(List<OurBookDto> dto);

    List<OurBookDto> select();

}
