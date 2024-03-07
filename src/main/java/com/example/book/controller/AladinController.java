package com.example.book.controller;

import com.example.book.dto.AladinDto;
import com.example.book.dto.Yes24Dto;
import com.example.book.service.AladinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;


@Api(tags = {"Aladin 정보를 제공하는 Controller"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/books")
public class AladinController {

    private final AladinService service;

    @ApiOperation(value = "aladin top 50위 리스트")
    @GetMapping("/aladin")
    public  List<AladinDto> AldinRankin() throws IOException{
        return service.getAladinTop50();


    }



}
