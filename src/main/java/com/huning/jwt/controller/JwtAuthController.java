package com.huning.jwt.controller;

import com.huning.jwt.common.util.JwtTokenizer;
import com.huning.jwt.domain.AccessToken;
import com.huning.jwt.domain.RefreshToken;
import com.huning.jwt.dto.AccountLogin;
import com.huning.jwt.service.AccountService;
import com.huning.jwt.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class JwtAuthController {

	private final JwtTokenizer jwtTokenizer;
	private final AccountService accountService;
	private final RefreshTokenService refreshTokenService;

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid AccountLogin loginDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		AccountLogin accountLogin = accountService.getAccountByAccount(loginDto.getAccount());

		if (!loginDto.getPassword().equals(accountLogin.getPassword())) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		String accessToken = jwtTokenizer.createAccessToken(accountLogin.getAccountId());
		String refreshToken = jwtTokenizer.createRefreshToken(accountLogin.getAccountId());

		RefreshToken refreshTokenEntity = new RefreshToken();
		refreshTokenEntity.setRefreshToken(refreshToken);
		refreshTokenEntity.setAccountId(accountLogin.getAccountId());
		refreshTokenService.addRefreshToken(refreshTokenEntity);

		ResponseCookie cookie = ResponseCookie.from("RefreshToken", refreshToken)
						.path("/")
						.httpOnly(true)
						.secure(false)
						.maxAge(Duration.ofDays(7))
						.sameSite("Strict")
						.build();

		AccessToken accessTokenDto = new AccessToken(accessToken);
		return ResponseEntity.ok()
						.header(HttpHeaders.SET_COOKIE, cookie.toString())
						.body(accessTokenDto);
	}

	@PostMapping("/logout")
	public ResponseEntity logout(@CookieValue("RefreshToken") String refreshToken) {
		refreshTokenService.deleteRefreshToken(refreshToken);
		return new ResponseEntity(HttpStatus.OK);
	}

	@PostMapping("/token")
	public ResponseEntity renewToken(@CookieValue("RefreshToken") String refreshToken) {
		RefreshToken findRefreshToken = refreshTokenService.findRefreshToken(refreshToken)
						.orElseThrow(() -> new IllegalArgumentException("Refresh token not found"));

		Claims claims = jwtTokenizer.parseRefreshToken(findRefreshToken.getRefreshToken());

		Long accountId = claims.get("accountId", Long.class);

		accountService.findByAccountId(accountId);

		String accessToken = jwtTokenizer.createAccessToken(accountId);

		AccessToken accessTokenDto = new AccessToken(accessToken);
		return new ResponseEntity(accessTokenDto, HttpStatus.OK);
	}
}
