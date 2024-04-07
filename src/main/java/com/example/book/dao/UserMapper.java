package com.example.book.dao;

import com.example.book.dto.BookReportDto;
import com.example.book.dto.HeartsDto;
import com.example.book.dto.LoginRequestDto;
import com.example.book.dto.BookeyUserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    //회원 가입
    void signUp(BookeyUserDto dto);

    //로그인
    int login(LoginRequestDto dto);

    //내정보 보기
    BookeyUserDto userInfoById(int idx);
    //내정보 수정
    void updateInfo(BookeyUserDto dto);

    //책 찜 갯수 수정
    void updateHeartCount(int idx);
    //좋아요 등록
    void heartTrue(HeartsDto heart);
    //등록된 좋아요 취소
    void heartFalse(HeartsDto heart);

    void updateHeartStatus(HeartsDto heart);
    //찜한 책목록
    List<Map<String,Object>> heartsList(int useridx);
    //유저가 쓴 독후감 리스트
    List<BookReportDto> userReportList(int useridx);

    String heartStatus(int useridx,int bookidx);
}
