package com.example.book.controller;

import com.example.book.dto.JWTTokenDto;
import com.example.book.dto.LoginRequestDto;
import com.example.book.dto.BookeyUserDto;
import com.example.book.service.JwtTokenService;
import com.example.book.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@Api(tags = {"User 와 관련된 Controller"})
@RequiredArgsConstructor
@RequestMapping("/user")
@PropertySource("classpath:application.properties")
public class UserController {

    private final UserService service;
    private final JwtTokenService jwt;

    @Value("${jwt.secretKey}")
    private String secretKey;
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
    @ApiOperation(value = "세션 로그인 방식")
    public int login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        LoginRequestDto dto = new LoginRequestDto(email,password);
        int idx = service.login(dto);

        if (idx != -1) {
            session.setAttribute("userIdx", idx);
        }

        return idx;
    }

    @PostMapping("/login2")
    @ApiOperation(value = "jwt 로그인 방식")
    public ResponseEntity<JWTTokenDto> login2(@Valid @RequestParam String email, @Valid @RequestParam String password) {
        LoginRequestDto dto = new LoginRequestDto(email, password);
        int idx = service.login(dto);

        if (idx != -1) {
            JWTTokenDto tokens = jwt.generateToken(idx);
            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/userinfo")
    @ApiOperation(value = "마이페이지 정보 확인")
    public ResponseEntity<BookeyUserDto> getUserInfo(@RequestParam("token") String token) {
        if (jwt.validateToken(token)) {
            int userIdx = jwt.getUserIdFromToken(token);
            BookeyUserDto userInfo = service.userInfoById(userIdx);
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/userupdate")
    @ApiOperation(value = "마이페이지 정보 업데이트")
    public ResponseEntity<String> updateUserInfo(@RequestParam("token") String token, @RequestBody BookeyUserDto dto) {
        if (jwt.validateToken(token)) {
            int userIdx = jwt.getUserIdFromToken(token);
            service.userInfoUpdate(userIdx,dto);
            return ResponseEntity.ok("사용자 정보가 업데이트되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
        }
    }


}
