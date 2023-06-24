package com.huning.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huning.jwt.domain.AccessToken;
import com.huning.jwt.dto.UserLoginDto;
import com.huning.jwt.util.JwtTokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class HomControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtTokenizer jwtTokenizer;

	@Test
	@DisplayName("로그인 성공 시 JSON 형태의 데이터가 응답된다.")
	void test1() throws Exception {
		// given
		UserLoginDto login = UserLoginDto.builder()
						.account("huning@gmail.com")
						.password("1234")
						.build();

		String json = objectMapper.writeValueAsString(login);

		// expected
		mockMvc.perform(post("/auth/login")
										.contentType(APPLICATION_JSON)
										.content(json))
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.accessToken").hasJsonPath())
						.andDo(print());
	}

	@Test
	@DisplayName("로그인 성공 시 응답되는 JSON 데이터는 AccessToken 객체 형태로 응답되어야 한다.")
	void test2() throws Exception {
		// given

		UserLoginDto login = UserLoginDto.builder()
						.account("huning@gmail.com")
						.password("1234")
						.build();

		String json = objectMapper.writeValueAsString(login);

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
		Assertions.assertDoesNotThrow(() -> {
			AccessToken accessToken = objectMapper.readValue(contentAsString, AccessToken.class);
		});
	}

	@Test
	@DisplayName("로그인 성공 시 응답되는 accessToken 값이 유효한지 확인한다.")
	void test3() throws Exception {
		// given

		UserLoginDto login = UserLoginDto.builder()
						.account("huning@gmail.com")
						.password("1234")
						.build();

		String json = objectMapper.writeValueAsString(login);

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
		Assertions.assertDoesNotThrow(() -> {
			AccessToken accessToken = objectMapper.readValue(contentAsString, AccessToken.class);
			jwtTokenizer.parseAccessToken(accessToken.getAccessToken());
		});
	}
}