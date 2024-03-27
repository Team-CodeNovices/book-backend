package com.example.book.dao;

import com.example.book.dto.LoginRequestDto;
import com.example.book.dto.BookeyUserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    //회원 가입
    void signUp(BookeyUserDto dto);

    //로그인
    String login(LoginRequestDto dto);
}
