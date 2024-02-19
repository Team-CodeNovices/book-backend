package com.example.book.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OurBookDto {

    private String bookname;        //책이름
    private String author;          //저자
    private String publisher;       //출판사
    private String genre;            //장르
    private String bookdetail;       //상세내용
    private String price;              //가격
    private String writedate;         //출판일

    @Builder(builderClassName = "OurBookBuilder") // 빌더 클래스의 이름을 명시적으로 지정
    public OurBookDto(String bookname,String author,String publisher,String bookdetail,String price,String writedate) {
        this.bookname = bookname;
        this.author = author;
        this.publisher = publisher;
        this.bookdetail = bookdetail;
        this.price = price;
        this.writedate = writedate;
    }


}
