package com.huning.jwt.controller;

import com.huning.jwt.common.util.JwtTokenizer;
import com.huning.jwt.domain.AccessToken;
import com.huning.jwt.dto.AccountLogin;
import com.huning.jwt.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class JwtAuthController {

	private final JwtTokenizer jwtTokenizer;
	private final AccountService accountService;

	@PostMapping("/login")
	public ResponseEntity login(@RequestBody @Valid AccountLogin loginDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		AccountLogin accountLogin = accountService.getAccountByAccount(loginDto.getAccount());

		String accessToken = jwtTokenizer.createAccessToken(accountLogin.getAccountId());
		String refreshToken = jwtTokenizer.createRefreshToken(accountLogin.getAccountId());

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
}
