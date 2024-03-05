package com.example.book.dto;

import lombok.*;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Yes24Dto extends OurBookDto {

        private int ranking;
        private String image;
        private String bookname;
        private String author;
        private String publisher;
        private String price;
        private String writedate;



}
