package com.example.book.controller;

import com.example.book.dto.Yes24Dto;
import com.example.book.service.Yes24Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/book")
public class Yes24Controller {

    private final Yes24Service service;

    @GetMapping("/yes24")
    public void yes24Ranking(Model model) throws IOException {
        List<Yes24Dto> list = service.getYes24Data();
        model.addAttribute("list", list);


    }




}
