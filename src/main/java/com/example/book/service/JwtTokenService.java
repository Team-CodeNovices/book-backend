package com.example.book.service;

import com.example.book.dto.JWTokenDto;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
//토큰이 유효한지 확인하는 클래스
public class JwtTokenService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    // 유저 정보를 가지고 AccessToken, RefreshToken을 생성하는 메서드
    public JWTokenDto generateToken(int idx) {
        // 새로운 Access Token 생성
        String accessToken = generateAccessToken(idx);

        // Refresh Token 생성
        String refreshToken = generateRefreshToken(idx);

        return JWTokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 토큰입니다", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT 클레임 문자열이 비어 있습니다.", e);
        }
        return false;
    }


    //토큰에서 필요한 정보 뽑기(일단은 유저id)
    public int getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }

    //access 토큰 발행하는 메소드
    private String generateAccessToken(int userIdx) {
        long nowMillis = System.currentTimeMillis();

        // Access Token 만료 시간 설정 (1시간)
        long accessTokenExpirationMillis = nowMillis + 3600000; // 1시간 (1시간 = 3600000밀리초)
        Date accessTokenExpiration = new Date(accessTokenExpirationMillis);

        // Access Token 생성
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userIdx))
                .setExpiration(accessTokenExpiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return accessToken;
    }

    //refresh 토큰 발행하는 메소드
    private String generateRefreshToken(int userIdx) {
        long nowMillis = System.currentTimeMillis();

        // Refresh Token 만료 시간 설정 (7일)
        long refreshTokenExpirationMillis = nowMillis + 7 * 24 * 3600000; // 7일 (1주일 = 7 * 24시간 * 3600000밀리초)
        Date refreshTokenExpiration = new Date(refreshTokenExpirationMillis);

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .claim("useridx", userIdx) // 사용자 ID를 클레임으로 추가
                .setExpiration(refreshTokenExpiration)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return refreshToken;
    }

    //access 토큰 만료시 refresh 토큰으로 재발급 받는 메소드(아직 사용 x)
    public JWTokenDto generateNewAccessToken(String refreshToken) {
        try {
            // Refresh 토큰을 파싱하여 유효성 검증
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            // Refresh 토큰에서 사용자 ID 가져오기
            int userIdx = Integer.parseInt(claims.get("useridx", String.class));

            // 새로운 Access Token 생성
            String newAccessToken = generateAccessToken(userIdx);

            return JWTokenDto.builder()
                    .accessToken(newAccessToken)
                    .build();
        } catch (JwtException e) {
            // Refresh 토큰이 유효하지 않은 경우
            log.error("Refresh 토큰이 유효하지 않습니다.", e);
            throw new RuntimeException("Refresh 토큰이 유효하지 않습니다.");
        }
    }
    

}
