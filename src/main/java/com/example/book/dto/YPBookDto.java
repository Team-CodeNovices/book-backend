package com.example.book.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class YPBookDto {
    private String bookname;
    private String author;
    private String publisher;
    private String bookInfo;
    private String publicationdate;
}