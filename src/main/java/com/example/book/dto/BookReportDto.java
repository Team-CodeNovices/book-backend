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
public class BookReportDto {
    private int  postidx;            //게시글 번호
    private int useridx;            //유저 번호
    private String reporttitle;     //게시글 제목
    private String postimage;       //책이미지
    private String bookname;        //책이름
    private String postcontents;    //작성글
    private Date writedate;        //작성일시
    private int userlike;           //좋아요 갯수
    private int commentscount;      // 댓글 갯수
    private int likecount;


}
