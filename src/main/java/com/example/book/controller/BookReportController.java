package com.example.book.controller;

import com.example.book.dto.BookReportDto;
import com.example.book.dto.ReportLikeDto;
import com.example.book.service.BookReportService;
import com.example.book.service.JwtTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = {"독후감 관련 Controller"})
@RequestMapping("/report")
public class BookReportController {

    private final BookReportService service;
    private final JwtTokenService service2;

    @ApiOperation("독후감 등록")
    @PostMapping("/registered")
    public void reportInsert(@RequestParam int useridx, @RequestParam String reporttitle, @RequestParam String postimage, @RequestParam String bookname, @RequestParam String postconetents ) {
        BookReportDto dto = new BookReportDto();
        dto.setUseridx(useridx);
        dto.setReporttitle(reporttitle);
        dto.setPostimage(postimage);
        dto.setBookname(bookname);
        dto.setPostcontents(postconetents);
     //   dto.setUserlike(userlike);
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
    public int updateReport(@RequestParam int postidx, @RequestParam String postcontents, @RequestParam String reporttitle,Authentication authentication) {
        int userIdx = Integer.parseInt(authentication.getName());
        //int userIdx = service2.getUserIdFromToken(token);
        BookReportDto dto = new BookReportDto();
        dto.setPostidx(postidx);
        dto.setPostcontents(postcontents);
        dto.setReporttitle(reporttitle);

        return service.reportupdate(dto,userIdx);
    }
    @ApiOperation("독후감 삭제")
    @DeleteMapping("/delete")
    public void reportdelete(@RequestParam  int postidx){
        BookReportDto dto = new BookReportDto();
        dto.setPostidx(postidx);

        service.reportdelete(dto);
    }

    @ApiOperation("독후감 좋아요 등록")
    @GetMapping("/liketure")
    public void reportlike(@RequestParam int user_id,@RequestParam int post_id){
        ReportLikeDto dto = new ReportLikeDto();
        dto.setUser_id(user_id);
        dto.setPost_id(post_id);
        service.liketrue(dto);
    }
    @ApiOperation("독후감 좋아요 취소")
    @GetMapping("/likefalse")
    public void reportfalse(@RequestParam int user_id,@RequestParam int post_id){
        ReportLikeDto dto = new ReportLikeDto();
        dto.setUser_id(user_id);
        dto.setPost_id(post_id);
        service.likefalse(dto);
    }


}
