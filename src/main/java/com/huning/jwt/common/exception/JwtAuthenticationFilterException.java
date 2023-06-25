package com.huning.jwt.common.exception;

import org.springframework.http.HttpStatus;

public class JwtAuthenticationFilterException extends AppException {

	private static final String MESSAGE = "인증 처리 과정에서 에러가 발생했습니다.";

	public JwtAuthenticationFilterException() {
		super(MESSAGE);
	}
	public JwtAuthenticationFilterException(String message) {
		super(message);
	}
	public JwtAuthenticationFilterException(Throwable cause) {
		super(MESSAGE, cause);
	}
	public JwtAuthenticationFilterException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.UNAUTHORIZED;
	}
}
