package com.huning.jwt;

import com.huning.jwt.domain.AccessToken;
import com.huning.jwt.dto.UserLoginDto;
import com.huning.jwt.util.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Duration;

@RestController
@RequiredArgsConstructor
public class HomController {

	private final JwtTokenizer jwtTokenizer;

	@PostMapping("/auth/login")
	public ResponseEntity login(@RequestBody @Valid UserLoginDto loginDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}


		String accessToken = jwtTokenizer.createAccessToken(1L);
		String refreshToken = jwtTokenizer.createRefreshToken(1L);

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
