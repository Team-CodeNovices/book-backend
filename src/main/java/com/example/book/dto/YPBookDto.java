package com.example.book.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YPBookDto {
    private int ranking;
    private String bookname;
    private String author;
    private String publisher;
    private String publicationdate;
}