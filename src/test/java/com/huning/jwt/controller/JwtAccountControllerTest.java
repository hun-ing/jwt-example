package com.huning.jwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huning.jwt.common.util.JwtTokenizer;
import com.huning.jwt.dto.AccountLogin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAccountControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtTokenizer jwtTokenizer;

	@Test
	@DisplayName("로그인 테스트 전에 계정을 만든다.")
	void before() throws Exception {
		// given
		AccountLogin accountLogin = AccountLogin.builder()
						.account("huning@gmail.com")
						.password("1234")
						.build();

		String json = objectMapper.writeValueAsString(accountLogin);

		// expected
		mockMvc.perform(post("/account")
										.contentType(APPLICATION_JSON)
										.content(json))
						.andExpect(status().is2xxSuccessful())
//						.andExpect(jsonPath("$.accessToken").hasJsonPath())
						.andDo(print());
	}
}