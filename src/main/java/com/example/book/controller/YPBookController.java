package com.example.book.controller;

import com.example.book.dto.YPBookDto;
import com.example.book.service.YPBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = {"yp 정보를 제공하는 controller"})
@RequiredArgsConstructor
@RequestMapping("/ypBook")
public class YPBookController {

    private final YPBookService service;

    @GetMapping("/ypBook")
    @ApiOperation(value = "yp top20위 리스트")
    public List<YPBookDto> yplist() throws IOException {
        return service.list();
    }
}