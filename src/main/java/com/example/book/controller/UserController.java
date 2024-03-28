package com.example.book.controller;

import com.example.book.dto.LoginRequestDto;
import com.example.book.dto.BookeyUserDto;
import com.example.book.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@Api(tags = {"User 정보를 제공하는 Controller"})
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입")
    public void signup(@RequestParam("username") String username,
                       @RequestParam("password") String password,
                       @RequestParam("email") String email,
                       @RequestParam("tel") String tel,
                       @RequestParam("address") String address,
                       @RequestParam("gender") String gender) {
        BookeyUserDto dto = BookeyUserDto.builder()
                .username(username)
                .password(password)
                .email(email)
                .tel(tel)
                .address(address)
                .gender(gender)
                .build();
        service.signUp(dto);
    }

    @GetMapping("/login")
    @ApiOperation(value = "로그인")
    public int login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        LoginRequestDto dto = new LoginRequestDto(email,password);
        int idx = service.login(dto);

        if (idx != -1) {
            session.setAttribute("userIdx", idx);
        }

        return idx;
    }


}
