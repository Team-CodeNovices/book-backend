package com.example.book.service;

import com.example.book.dao.UserMapper;
import com.example.book.dto.LoginRequestDto;
import com.example.book.dto.BookeyUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserMapper dao;

    //회원 가입
    public void signUp(BookeyUserDto dto) {
        dao.signUp(dto);
    }

    //로그인
    public String login(LoginRequestDto dto) {
        String id = dao.login(dto);
        log.info(id);
        return id;
    }

}
