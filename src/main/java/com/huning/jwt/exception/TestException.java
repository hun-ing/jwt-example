package com.huning.jwt.exception;

import org.springframework.http.HttpStatus;

public class TestException extends AppException {

	private static final String MESSAGE = "Test 에러";

	public TestException() {
		super(MESSAGE);
	}
	public TestException(String message) {
		super(message);
	}
	public TestException(Throwable cause) {
		super(MESSAGE, cause);
	}
	public TestException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.UNAUTHORIZED;
	}
}
