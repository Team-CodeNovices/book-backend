package com.example.book.controller;

import com.example.book.dto.OurBookDto;
import com.example.book.dto.RecommendBooksDto;
import com.example.book.service.OurBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = {"OurBook 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RequestMapping("/ourbook")

public class OurBookController {

    private final OurBookService book;

    @ApiOperation(value = "OurBook 전체 리스트")
    @GetMapping("/all")
    public List<OurBookDto> selectList() {
        return book.selectlist();
    }

    @ApiOperation(value = "책관련 추천 도서")
    @GetMapping("/recommend")
    public List<RecommendBooksDto> recommendList(@RequestParam String bookname) {
        return book.recommendedBooks(bookname);
    }

    @ApiOperation(value = "에디터 추천 도서")
    @GetMapping("/editor")
    public List<RecommendBooksDto> editorPicks() throws IOException {
        return book.randomBooksFromTopN(11, 50, 3);
    }

    @ApiOperation(value = "베스트 추천 도서")
    @GetMapping("/best")
    public List<RecommendBooksDto> bestPicks() throws IOException {
        return book.randomBooksFromTopN(1, 10, 5);
    }

}
