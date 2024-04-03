package com.example.book.controller;

import com.example.book.dto.ReportcommentsDto;
import com.example.book.service.ReportcommentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/reportcomment")
@RestController
@Api(tags = {"댓글 관련 컨트롤러"})
public class ReportcommentController {

    private final ReportcommentsService service;
    @PostMapping("/comment")
    @ApiOperation(value = "댓글작성")
    public void commentinsert(@RequestBody ReportcommentsDto dto){
     service.conmmentinsert(dto);
    }

    @PostMapping("/commentupdate")
    @ApiOperation(value = "댓글 수정")
    public void commentupdate(@RequestBody ReportcommentsDto dto) {
        service.commentupdate(dto);
    }

    @DeleteMapping("/commentdelete")
    @ApiOperation(value = "댓글 삭제")
    public void commentdelete(int commentidx){
        service.commentdelete(commentidx);
    }

    @GetMapping("/commentslist")
    @ApiOperation(value = "댓글 리스트")
    public List<ReportcommentsDto> commentslist(@RequestParam int postidx) {return service.reportcommentlist(postidx);}
}
