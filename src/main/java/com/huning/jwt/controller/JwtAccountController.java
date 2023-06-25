package com.huning.jwt.controller;

import com.huning.jwt.common.util.JwtTokenizer;
import com.huning.jwt.dto.AccountLogin;
import com.huning.jwt.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class JwtAccountController {

	private final JwtTokenizer jwtTokenizer;
	private final AccountService accountService;

	@PostMapping
	public ResponseEntity signup(@RequestBody AccountLogin accountLogin, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		Long accountId = accountService.addAccount(accountLogin);

		accountLogin.setAccountId(accountId);

		System.out.println("accountId = " + accountId);

		return ResponseEntity.status(HttpStatus.CREATED).body(accountLogin);
	}
}
