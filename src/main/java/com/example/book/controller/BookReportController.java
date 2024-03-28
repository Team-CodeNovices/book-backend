package com.example.book.controller;

import com.example.book.dto.BookReportDto;
import com.example.book.service.BookReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
@Api(tags = {"독후감 관련 Controller"})
@RequestMapping("/report")
public class BookReportController {

    private final BookReportService service;

    @ApiOperation("독후감 등록")
    @PostMapping("/registered")
    public void reportInsert(@RequestParam String postimage, @RequestParam String bookname, @RequestParam String postconetents, HttpSession session) {
        BookReportDto dto = new BookReportDto();
//        int useridx = (int)session.getAttribute("userIdxs");
//        dto.setUseridx(useridx);
        dto.setPostimage(postimage);
        dto.setBookname(bookname);
        dto.setPostcontents(postconetents);
        service.reportInsert(dto);
    }


}
