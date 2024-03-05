package com.example.book.controller;

import com.example.book.dto.AladinDto;
import com.example.book.dto.Yes24Dto;
import com.example.book.service.AladinService;
import com.example.book.service.Yes24Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

//@Controller
@Api(tags = {"Yes24 정보를 제공하는 Controller"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class Yes24Controller {

    private final Yes24Service service;
    private final AladinService service2;

    //restController
    @ApiOperation(value = "yes24 top50위 리스트")
    @GetMapping("/yes24")
    public void yes24Ranking(Model model) throws IOException {
        List<Yes24Dto> list = service.getYes24Top50();
        model.addAttribute("list", list);
    }

    @GetMapping("/aladin")
    public void aladinRanking(Model model) throws IOException {
        List<AladinDto> list = service2.getAladinTop50();
        model.addAttribute("list", list);

    }

    //controller
//    @GetMapping("/yes24")
//    public void yes24Ranking(Model model) throws IOException {
//        List<Yes24Dto> list = service.getYes24Top50();
//        model.addAttribute("list", list);
//    }


}
