package com.example.book.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AladinDto extends OurBookDto{
    private int ranking;
    private String bookname;
    private String author;
    private String publisher;
    private String genre;
    private String bookdetail;
    private String price;
    private String writedate;
}
