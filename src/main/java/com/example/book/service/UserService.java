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
    private final JwtTokenService jwtService;

    //회원 가입
    public void signUp(BookeyUserDto dto) {
        dao.signUp(dto);
    }

    //로그인
    public int login(LoginRequestDto dto) {
        int id = dao.login(dto);
        return id;
    }

    //내정보 보기
    public BookeyUserDto userInfoById(int idx) {
             return dao.userInfoById(idx);
    }

    //내정보 수정
    public void userInfoUpdate(BookeyUserDto dto) {
        dao.updateInfo(dto);
    }
    
    //이후에 인증이 필요한 메소드는 이렇게 사용하라고 남겨놓은겁니다.
    public void jwtProvideExample(String token) {
        // 토큰 검증하기
        if(jwtService.validateToken(token)) {
            // 토큰이 유효한 경우
            int userId = jwtService.getUserIdFromToken(token);
            // 여기서 userId를 사용하여 사용자 정보를 수정
//            userService.updateUserInfo(userId, newUserInfo);
        } else {
            // 토큰이 유효하지 않은 경우 처리
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }

}
