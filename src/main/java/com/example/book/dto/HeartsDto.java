package com.example.book.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HeartsDto {

    private int useridx;             //좋아요 누른 사용자
    private int bookidx;             //책 번호
    private String status;          //찜 체크박스 상태

}
