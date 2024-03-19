package com.example.book.controller;

import com.example.book.dto.OurBookDto;
import com.example.book.dto.RecommendBooksDto;
import com.example.book.service.OurBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
@RestController
@Api(tags = {"OurBook 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RequestMapping("/ourbook")

//전체 리스트 보기
public class OurBookController {

    private final OurBookService book;

    @ApiOperation(value = "OurBook 전체 리스트")
    @GetMapping("/all")
    public List<OurBookDto> selectList() throws IOException {
        return book.selectlist();
    }
    
    @ApiOperation(value = "OurBook 추천 도서")
    @GetMapping("/recommend")
    public List<RecommendBooksDto> recommendList() throws IOException {
        return book.getRandomBooks();
    }
    
    

}
