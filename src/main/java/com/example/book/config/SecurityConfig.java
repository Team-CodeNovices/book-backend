//package com.example.book.config;
//
//import com.example.book.service.JwtTokenService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig extends WebSecurityConfiguration  {
//
//    private final JwtTokenService jwtTokenService;
//
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable() // CSRF 보안 비활성화
//                .authorizeRequests()
//                .anyRequest().authenticated() // 모든 요청에 대해 인증 필요
//                .and()
//                .addFilterBefore(new JwtTokenAuthenticationFilter(jwtTokenService), UsernamePasswordAuthenticationFilter.class); // JwtTokenAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 추가
//    }
//}
