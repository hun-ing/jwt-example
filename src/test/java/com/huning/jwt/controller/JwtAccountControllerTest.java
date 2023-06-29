package com.huning.jwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huning.jwt.common.util.JwtTokenizer;
import com.huning.jwt.domain.AccessToken;
import com.huning.jwt.dto.AccountLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

	private String accessToken;

	private AccountLogin accountLogin = AccountLogin.builder()
					.account("huning@gmail.com")
					.password("1234")
					.build();

	@BeforeEach
	@DisplayName("계정을 만들고 로그인 후 토큰을 저장한다.")
	void before() throws Exception {
		String json = objectMapper.writeValueAsString(accountLogin);

		// expected
		mockMvc.perform(post("/account")
										.contentType(APPLICATION_JSON)
										.content(json))
						.andExpect(status().is2xxSuccessful())
						.andExpect(jsonPath("$.accountId").isNotEmpty())
						.andExpect(jsonPath("$.account").value(accountLogin.getAccount()))
						.andExpect(jsonPath("$.password").value(accountLogin.getPassword()))
						.andDo(print());

		// when
		String contentAsString = mockMvc.perform(post("/auth/login")
										.contentType(APPLICATION_JSON)
										.content(json))
						.andExpect(status().isOk())
						.andDo(print())
						.andReturn()
						.getResponse()
						.getContentAsString();

		// then
		assertThatCode(() -> {
			AccessToken responseAccessToken = objectMapper.readValue(contentAsString, AccessToken.class);
			jwtTokenizer.parseAccessToken(responseAccessToken.getAccessToken());
			accessToken = responseAccessToken.getAccessToken();
		});
	}

	@Test
	@DisplayName("accessToken을 통해 계정 정보를 가져온다.")
	void test2() throws Exception {
		// given
		String contentAsString = mockMvc.perform(get("/account")
										.header("Authorization", "Bearer " + accessToken))
						.andExpect(status().is2xxSuccessful())
						.andExpect(jsonPath("$.accountId").isNotEmpty())
						.andExpect(jsonPath("$.account").value(accountLogin.getAccount()))
						.andExpect(jsonPath("$.password").value(accountLogin.getPassword()))
						.andDo(print()).andReturn().getResponse().getContentAsString();


		// when
		AccountLogin responseAccountLogin = objectMapper.readValue(contentAsString, AccountLogin.class);

		// then
		assertThat(responseAccountLogin.getAccountId()).isNotNull();
		assertThat(responseAccountLogin.getAccount()).isEqualTo(accountLogin.getAccount());
		assertThat(responseAccountLogin.getPassword()).isEqualTo(accountLogin.getPassword());
	}
}