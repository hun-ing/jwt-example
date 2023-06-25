package com.huning.jwt.common.util;

import com.huning.jwt.common.config.AppConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenizer {

	private final AppConfig appConfig;

	public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 30 * 60 * 1000L; // 30 minutes
	public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days

	/**
	 * AccessToken 생성
	 */
	public String createAccessToken(Long userId) {
		return createToken(userId, ACCESS_TOKEN_EXPIRE_COUNT, appConfig.getJwtUtil().getJwtKey());
	}

	/**
	 * RefreshToken 생성
	 */
	public String createRefreshToken(Long userId) {
		return createToken(userId, REFRESH_TOKEN_EXPIRE_COUNT, appConfig.getJwtUtil().getJwtKey());
	}


	private String createToken(Long userId, Long expire, byte[] secretKey) {
		Claims claims = Jwts.claims();
		claims.put("userId", userId);
		return Jwts.builder()
						.setClaims(claims)
						.setIssuedAt(new Date())
						.setExpiration(new Date(new Date().getTime() + expire))
						.signWith(getSigningKey(secretKey))
						.compact();
	}

	/**
	 * 토큰에서 유저 아이디 얻기
	 */
	public Long getUserIdFromToken(String token) {
		String[] tokenArr = token.split(" ");
		token = tokenArr[1];
		Claims claims = parseToken(token, appConfig.getJwtUtil().getJwtKey());
		return Long.valueOf((Integer) claims.get("userId"));
	}

	public Claims parseAccessToken(String accessToken) {
		return parseToken(accessToken, appConfig.getJwtUtil().getJwtKey());
	}

	public Claims parseRefreshToken(String refreshToken) {
		return parseToken(refreshToken, appConfig.getJwtUtil().getJwtKey());
	}


	public Claims parseToken(String token, byte[] secretKey) {
		return Jwts.parserBuilder()
						.setSigningKey(getSigningKey(secretKey))
						.build()
						.parseClaimsJws(token)
						.getBody();
	}

	/**
	 * @param secretKey - byte형식
	 * @return Key 형식 시크릿 키
	 */
	public static Key getSigningKey(byte[] secretKey) {
		return Keys.hmacShaKeyFor(secretKey);
	}

}
