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
    private int commentsidx;
    private int postidx;
    private int useridx;
    private String content;
    private Date writedate;
}
