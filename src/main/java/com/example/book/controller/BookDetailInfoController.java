package com.example.book.controller;


import com.example.book.dto.OurBookDto;
import com.example.book.service.BookDetailInfo;
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
@Api(tags = {"책 상세내용 API"})
@RequiredArgsConstructor
@RequestMapping("/detail")
public class BookDetailInfoController {

    private final BookDetailInfo detail;

    @ApiOperation(value = "책 상세내용")
    @GetMapping("/book")
    public List<OurBookDto> Bookdetailinfo(@RequestParam String bookname) throws IOException{
        return detail.Bookdetailinfo(bookname);
    }
}
