package com.example.book.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "rankingDtoBuilder")
public class RankingDto {

    private int ranking;
    private String image;
    private String bookname;
    private String author;
    private String publisher;
}
