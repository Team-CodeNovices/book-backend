package com.example.book.config;

import com.example.book.service.JwtTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class JWTokenFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    public JWTokenFilter(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // HTTP 요청에서 헤더에서 JWT 토큰 추출
        String token = extractToken(request);

        // 토큰이 존재하고 유효한 경우
        if (token != null && jwtTokenService.validateToken(token)) {
            // 사용자 ID 추출
            int userId = jwtTokenService.getUserIdFromToken(token);
            // 사용자 정보를 설정
            UserDetails userDetails = new User(String.valueOf(userId), "", new ArrayList<>());

            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // SecurityContextHolder에 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    // HTTP 요청에서 Authorization 헤더에서 JWT 토큰 추출
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // "Bearer " 이후의 토큰 부분 반환
        }
        return null;
    }
}
