package com.example.book.controller;

import com.example.book.dto.*;
import com.example.book.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

//@Controller
@RestController
@Api(tags = {"Ranking 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RanKingController {


    private final AladinService abook;
    private final Yes24Service yesbook;
    private final YPBookService ypbook;


    //알라딘 리스트
    @ApiOperation(value = "aladin top 50위 리스트")
    @GetMapping("/aladin")
    public List<RankingDto> AldinRankin() throws IOException {
        return abook.getAladinTop50();
    }

    //yes24 리스트
    @ApiOperation(value = "yes24 top50위 리스트")
    @GetMapping("/yes24")
    public List<RankingDto> yes24Ranking() throws IOException {
        return yesbook.getYes24Top50();
    }

    //영풍문고 리스트
    @GetMapping("/ypBook")
    @ApiOperation(value = "yp top20위 리스트")
    public List<RankingDto> yplist() throws IOException {
        return ypbook.list();
    }


}
