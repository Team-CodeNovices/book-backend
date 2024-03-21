package com.example.book.controller;


import com.example.book.dto.OurBookDto;
import com.example.book.service.BookDetailInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = {"책,작가,출판사 내용 API"})
@RequiredArgsConstructor
@RequestMapping("/detail")
public class BookDetailInfoController {

    private final BookDetailInfoService detail;

    @ApiOperation(value = "책 상세내용")
    @GetMapping("/book/{bookname}")
    public List<OurBookDto> Bookdetailinfo(@PathVariable String bookname) throws IOException{
        return detail.Bookdetailinfo(bookname);
    }

}
