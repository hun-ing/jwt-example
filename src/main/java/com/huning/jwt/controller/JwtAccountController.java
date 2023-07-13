package com.huning.jwt.controller;

import com.huning.jwt.common.util.AccountDetail;
import com.huning.jwt.common.util.JwtTokenizer;
import com.huning.jwt.common.util.Verify;
import com.huning.jwt.dto.AccountLogin;
import com.huning.jwt.service.AccountService;
import com.huning.jwt.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class JwtAccountController {

	private final JwtTokenizer jwtTokenizer;
	private final AccountService accountService;
	private final RefreshTokenService refreshTokenService;

	@PostMapping
	public ResponseEntity signup(@RequestBody AccountLogin accountLogin, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		Long accountId = accountService.addAccount(accountLogin);
		accountLogin.setAccountId(accountId);
		return ResponseEntity.status(HttpStatus.CREATED).body(accountLogin);
	}

	@GetMapping
	public ResponseEntity getAccount(@Verify AccountDetail accountDetail) {
		AccountLogin accountLogin = accountService.findByAccountId(accountDetail.getAccountId());
		return new ResponseEntity(accountLogin, HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity deleteAccount(@Verify AccountDetail accountDetail, @CookieValue("RefreshToken") String refreshToken) {
		accountService.deleteAccount(accountDetail.getAccountId());
		refreshTokenService.deleteRefreshToken(refreshToken);
		return new ResponseEntity(HttpStatus.OK);
	}
}
