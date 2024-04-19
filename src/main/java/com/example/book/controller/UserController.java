package com.example.book.controller;

import com.example.book.dto.*;
import com.example.book.service.JwtTokenService;
import com.example.book.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = {"User 와 관련된 Controller"})
@RequiredArgsConstructor
@RequestMapping("/user")
@PropertySource("classpath:application.properties")
@Slf4j
public class UserController {

    private final UserService service;
    private final JwtTokenService jwt;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입")
    public void signup(@RequestBody BookeyUserDto dto) {
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
        dto.setUseridx(userIdx);

        service.userInfoUpdate(dto);

        return ResponseEntity.ok("사용자 정보가 업데이트되었습니다.");
    }

    @PostMapping("/heartupdate")
    @ApiOperation(value = "찜 등록 또는 취소")
    public void handleHeartAction(
            @RequestParam int bookidx,
            @RequestParam int userIdx,
            @RequestParam("찜등록 check,찜 취소 uncheck") String action) {

        HeartsDto heartsDto = new HeartsDto();
        heartsDto.setBookidx(bookidx);
        heartsDto.setUseridx(userIdx);

        if ("check".equals(action)) {
            String currentStatus = service.getHeartStatus(userIdx, bookidx);
            if (currentStatus == null) {
                heartsDto.setStatus("liked");
                service.heartTrue(heartsDto);
            } else if (!currentStatus.equals("unliked")) {
                heartsDto.setStatus("liked");
                service.updateHeartStatus(heartsDto);
            }
        }
        else if ("uncheck".equals(action)) {
            service.heartFalse(heartsDto);
        }
    }

    @GetMapping("/heartslist")
    @ApiOperation(value = "찜한 책리스트")
    public ResponseEntity<List<Map<String, Object>>> getHeartsByUserId(@RequestParam int userIdx) {
//        int userIdx = Integer.parseInt(authentication.getName());

        List<Map<String, Object>> hearts = service.heartsList(userIdx);
        return ResponseEntity.ok(hearts);
    }

    @GetMapping("/reportlist")
    @ApiOperation(value = "내가 쓴 독후감 리스트")
    public ResponseEntity<List<BookReportDto>> getReportByUserId(@RequestParam int useridx) {
//        int userIdx = Integer.parseInt(authentication.getName());

        List<BookReportDto> list = service.userReport(useridx);
        log.info("컨트롤러 통과" + useridx);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/heartStatus")
    @ApiOperation(value = "하트 상태")
    public ResponseEntity<String> getHeartStatus(
            @RequestParam("useridx") int useridx,
            @RequestParam("bookidx") int bookidx) {

        String status = service.getHeartStatus(useridx, bookidx);

        return ResponseEntity.ok(status);
    }

}
