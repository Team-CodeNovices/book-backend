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
public class OurBookService {

    private final OurBookMapper dao;

    //ourbook 리스트 불러오기
    public List<OurBookDto> selectlist() throws IOException {
        return dao.select();
    }

    //키워드 검색
    public List<OurBookDto> searchByKeyword(String keyword) throws IOException {
        keyword = keyword.replace("\\s","");
        if (keyword.length() < 2){
            return new ArrayList<>();
        }
        return dao.searchKeyword(keyword);
    }


}