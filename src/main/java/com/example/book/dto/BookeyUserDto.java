package com.example.book.dto;

import lombok.*;

import javax.validation.constraints.Pattern;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookeyUserDto {

    private String idx;

    @Pattern(regexp = "^[a-zA-Z가-힣]{2,}$",message = "이름: 영대소문자와 한글만 가능 2글자 이상으로 하세요.")
    private String username;

    private String nickname;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[`~!@#$%^&*()-_=+]).{8,24}$"
            ,message = "패스워드: 영문자,기호,숫자를 반드시 1개 포함하여 8~24 글자로 하세요.")
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?$"
            ,message = "이메일: 작성규칙이 올바르지 않습니다.")
    private String email;

    private String tel;
    private String address;
    private int heart;
    private Date joindate;
    private String gender;


}
