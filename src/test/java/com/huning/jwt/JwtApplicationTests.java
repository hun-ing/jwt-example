package com.huning.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class JwtApplicationTests {

	@Test
	void contextLoads() {
		Optional.ofNullable("").ifPresentOrElse(
						value -> {
							System.out.println("값이 있습니다 = " + value);
						},
						() -> {
							System.out.println("값이 없습니다.");
						}
		);
	}

}
