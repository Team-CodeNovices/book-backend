package com.example.book.controller;

import com.example.book.dto.JWTokenDto;
import com.example.book.dto.LoginRequestDto;
import com.example.book.dto.BookeyUserDto;
import com.example.book.service.JwtTokenService;
import com.example.book.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(tags = {"User 와 관련된 Controller"})
@RequiredArgsConstructor
@RequestMapping("/user")
@PropertySource("classpath:application.properties")
public class UserController {

    private final UserService service;
    private final JwtTokenService jwt;

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

    @PostMapping("/login")
    @ApiOperation(value = "jwt 로그인 방식")
    public ResponseEntity<JWTokenDto> login2(@Valid @RequestParam String email, @Valid @RequestParam String password) {
        LoginRequestDto dto = new LoginRequestDto(email, password);
        int idx = service.login(dto);

        if (idx != -1) {
            JWTokenDto tokens = jwt.generateToken(idx);
            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/userinfo")
    @ApiOperation(value = "마이페이지 정보 확인")
    public ResponseEntity<BookeyUserDto> getUserInfo(Authentication authentication) {
        int userIdx = Integer.parseInt(authentication.getName());
        return ResponseEntity.ok(service.userInfoById(userIdx));
    }

    @PutMapping("/userupdate")
    @ApiOperation(value = "마이페이지 정보 업데이트")
    public ResponseEntity<String> updateUserInfo(Authentication authentication,
                                                 @RequestParam BookeyUserDto dto) {
        int userIdx = Integer.parseInt(authentication.getName());
        dto.setIdx(userIdx);

        service.userInfoUpdate(dto);

        return ResponseEntity.ok("사용자 정보가 업데이트되었습니다.");
    }
}
