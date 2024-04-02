package com.example.book.controller;

import com.example.book.dto.BookReportDto;
import com.example.book.service.BookReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"독후감 관련 Controller"})
@RequestMapping("/report")
public class BookReportController {

    private final BookReportService service;

    @ApiOperation("독후감 등록")
    @PostMapping("/registered")
    public void reportInsert(@RequestParam int useridx, @RequestParam String reporttitle, @RequestParam String postimage, @RequestParam String bookname, @RequestParam String postconetents, @RequestParam int userlike) {
        BookReportDto dto = new BookReportDto();
        dto.setUseridx(useridx);
        dto.setReporttitle(reporttitle);
        dto.setPostimage(postimage);
        dto.setBookname(bookname);
        dto.setPostcontents(postconetents);
        dto.setUserlike(userlike);
        service.reportInsert(dto);
    }

    @ApiOperation("독후감 전체보기")
    @GetMapping("/reportall")
    public List<BookReportDto> reportselect() {
        return service.reportall();
    }

    @ApiOperation("독후감 상세보기")
    @GetMapping("/reportselect")
    public BookReportDto reportselect(BookReportDto dto){
        return service.reportselect(dto);
    }

    @ApiOperation("독후감 수정")
    @PutMapping("/update")
    public int reportupdate(@RequestParam int postidx, @RequestParam String postcontents, @RequestParam String reporttitle) {
        BookReportDto dto = new BookReportDto();
        dto.setPostidx(postidx);
        dto.setPostcontents(postcontents);
        dto.setReporttitle(reporttitle);

        return service.reportupdate(dto);
    }
    @ApiOperation("독후감 삭제")
    @DeleteMapping("/delete")
    public void reportdelete(@RequestParam  int postidx){
        BookReportDto dto = new BookReportDto();
        dto.setPostidx(postidx);

        service.reportdelete(dto);
    }


}
