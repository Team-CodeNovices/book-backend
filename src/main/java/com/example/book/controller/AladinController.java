package com.example.book.controller;

import com.example.book.dto.AladinDto;
import com.example.book.dto.Yes24Dto;
import com.example.book.service.AladinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Controller
@RequiredArgsConstructor
@RequestMapping("/aladin")
public class AladinController {

    private final AladinService service;

    @GetMapping("/aladin")
    public void aladinRanking(Model model) throws IOException {
        List<AladinDto> list = service.getAladinAllData();
        model.addAttribute("list", list);

    }



}
