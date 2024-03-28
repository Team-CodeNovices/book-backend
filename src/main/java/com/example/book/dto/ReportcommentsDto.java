package com.example.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportcommentsDto {
    private int commentsidx;        //댓글 번호
    private int postidx;            //게시글 번호
    private int useridx;            //유저 번호
    private String content;         //댓글
    private Date writedate;         //작성일시
}
