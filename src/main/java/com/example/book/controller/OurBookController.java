package com.example.book.controller;

import com.example.book.dto.OurBookDto;
import com.example.book.service.OurBookService;
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
//@Controller
@RestController@Api(tags = {"OurBook 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RequestMapping("/ourbook")
public class OurBookController {

    private final OurBookService book;

    //ourbook 리스트 보기
    @ApiOperation(value = "OurBook 전체 리스트")
    @GetMapping("/all")
    public List<OurBookDto> selectList(Model model) throws IOException {
        return book.selectlist();
    }

//    @GetMapping("/all")
//    public void selectList(Model model) throws IOException {
//        List<OurBookDto> list = book.selectlist();
//        model.addAttribute("list",list);
//    }



}
