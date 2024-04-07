package com.example.book.service;

import com.example.book.dao.UserMapper;
import com.example.book.dto.BookReportDto;
import com.example.book.dto.HeartsDto;
import com.example.book.dto.LoginRequestDto;
import com.example.book.dto.BookeyUserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    //찜 등록
    public void heartTrue(HeartsDto heart) {
        dao.heartTrue(heart);
        dao.updateHeartCount(heart.getBookidx());
    }

    //찜 취소
    public void heartFalse(HeartsDto heart) {
        dao.heartFalse(heart);
        dao.updateHeartCount(heart.getBookidx());

    }

    //찜 status 상태 업데이트
    public void updateHeartStatus(HeartsDto heart) {
        dao.updateHeartStatus(heart);
    }

    //유저가 찜한 책 리스트
    public List<Map<String, Object>> heartsList(int useridx) {
        return dao.heartsList(useridx);
    }

    //유저가 쓴 독후감 리스트    
    public List<BookReportDto> userReport(int useridx) {
        return dao.userReportList(useridx);
    }

    //좋아요 상태
    public String getHeartStatus(int useridx, int bookidx) {
        return dao.heartStatus(useridx, bookidx);
    }

}
