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
public class OurBookDto {

    private String image;           //이미지 링크
    private String bookname;        //책이름
    private String author;          //저자
    private String publisher;       //출판사
    private String genre;            //장르
    private String bookdetail;       //상세내용
    private String price;              //가격
    private String writedate;         //출판일
    private String mainkeyword;       //주요 키워드


    @Builder(builderClassName = "OurBookBuilder") // 빌더 클래스의 이름을 명시적으로 지정
    public OurBookDto(String image,String bookname,String author,String publisher,String bookdetail,String genre,String price,String writedate) {
        this.image = image;
        this.bookname = bookname;
        this.author = author;
        this.publisher = publisher;
        this.bookdetail = bookdetail;
        this.genre = genre;
        this.price = price;
        this.writedate = writedate;
    }

    @Override
    public String toString() {
        return "OurBookDto{" +
                "image='" + image + '\'' +
                ", bookname='" + bookname + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", genre='" + genre + '\'' +
                ", bookdetail='" + bookdetail + '\'' +
                ", price='" + price + '\'' +
                ", writedate='" + writedate + '\'' +
                '}';
    }

}
