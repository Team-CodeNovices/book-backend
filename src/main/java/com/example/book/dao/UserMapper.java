package com.example.book.dao;

import com.example.book.dto.LoginRequestDto;
import com.example.book.dto.BookeyUserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    //회원 가입
    void signUp(BookeyUserDto dto);

    //로그인
    int login(LoginRequestDto dto);

    //내정보 보기
    BookeyUserDto userInfoById(int idx);
    //내정보 수정
    void updateInfo(int idx,BookeyUserDto dto);
}
