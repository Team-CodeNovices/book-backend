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
    private int postidx;
    private int useridx;
    private String postimage;
    private String bookname;
    private String postcontents;
    private Date writedate;
    private int userlike;

}
