package com.example.book.controller;

import com.example.book.dto.OurBookDto;
import com.example.book.service.BookApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
//@RestController
public class BookApiController {

    private final BookApiService service;

    @GetMapping("/bookapi")
    public void selectapi(Model model) throws IOException {
        List<OurBookDto> list = service.selectList();
//        service.updateBooksFromApi();
        service.keywordFromApi();
        model.addAttribute("list",list);
    }



}
