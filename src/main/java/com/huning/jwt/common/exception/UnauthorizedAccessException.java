package com.huning.jwt.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends AppException {

	private static final String MESSAGE = "접근이 거부되었습니다.";

	public UnauthorizedAccessException() {
		super(MESSAGE);
	}
	public UnauthorizedAccessException(String message) {
		super(message);
	}
	public UnauthorizedAccessException(Throwable cause) {
		super(MESSAGE, cause);
	}
	public UnauthorizedAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.UNAUTHORIZED;
	}
}
