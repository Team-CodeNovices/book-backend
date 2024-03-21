package com.example.book.dto;

import com.example.book.service.BookApiService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder(builderMethodName = "ourBookDtoBuilder")
public class OurBookDto {

    private String link;                //책 링크
    private String image;               //이미지 링크
    private String bookname;            //책이름
    private String author;              //저자
    private String publisher;           //출판사
    private String genre;               //장르
    private String contents;            //목차
    private String bookdetail;          //상세내용
    private String authordetail;        //저자소개
    private String price;               //가격
    private String writedate;           //출판일
    private String mainkeyword;         //주요 키워드
    private String assistkeyword;       //어시스턴트 키워드


}
