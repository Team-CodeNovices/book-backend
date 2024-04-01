//package com.example.book.config;
//
//import com.example.book.service.JwtTokenService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//@RequiredArgsConstructor
//public class JwtTokenAuthenticationFilter extends GenericFilter {
//    private final JwtTokenService jwtTokenService;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//        // HTTP 요청에서 토큰 추출
//        String token = extractTokenFromRequest(httpRequest);
//
//        if (token != null && jwtTokenService.validateToken(token)) {
//            // 토큰의 유효성 검사 후 인증 정보 설정
//            int userIdx = jwtTokenService.getUserIdFromToken(token);
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userIdx, null);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//
//        chain.doFilter(request, response);
//    }
//
//    private String extractTokenFromRequest(HttpServletRequest request) {
//        // 실제로는 헤더나 쿼리 파라미터 등에서 토큰을 추출하는 로직을 구현해야 합니다.
//        // 예시로는 Authorization 헤더에서 Bearer 토큰을 추출하는 방법을 사용합니다.
//        String bearerToken = request.getHeader("Authorization");
//        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7); // "Bearer " 이후의 토큰 부분만 추출
//        }
//        return null;
//    }
//}
