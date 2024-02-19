package com.example.book.controller;

import com.example.book.dto.OurBookDto;
import com.example.book.service.OurBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
@Controller
@RequiredArgsConstructor
@RequestMapping("/ourbook")
public class OurBookController {

    private final OurBookService book;

    //ourbook 리스트 보기
    @GetMapping("/all")
    public void selectList(Model model) throws IOException {
        List<OurBookDto> list = book.selectlist();
        model.addAttribute("list",list);
    }



}
