package com.example.book.controller;

import com.example.book.service.BookApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Api(tags = {"api 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookApiController {

    private final BookApiService service;

    @ApiOperation(value = "api 정보 업데이트")
    @GetMapping("/bookapi")
    public void updateFromApi() throws IOException {
        service.updateBooksFromApi();
    }



}
