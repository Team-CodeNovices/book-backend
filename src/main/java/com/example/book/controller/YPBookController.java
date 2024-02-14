package com.example.book.controller;

import com.example.book.dto.YPBookDto;
import com.example.book.service.YPBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ypBook")
public class YPBookController {

    private final YPBookService service;

    @GetMapping("/ypbook")
    public void list(Model model) throws IOException {
        List<YPBookDto> list = service.list();
        model.addAttribute("list",list);
    }
}