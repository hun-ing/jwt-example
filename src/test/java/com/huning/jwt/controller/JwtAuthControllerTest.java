package com.huning.jwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huning.jwt.common.util.JwtTokenizer;
import com.huning.jwt.domain.AccessToken;
import com.huning.jwt.domain.RefreshToken;
import com.huning.jwt.dto.AccountLogin;
import com.huning.jwt.reposiroty.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthControllerTest {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private JwtTokenizer jwtTokenizer;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	private AccountLogin account;

	@BeforeEach
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
						.andExpect(status().is(201))
						.andExpect(jsonPath("$.accountId").isNotEmpty())
						.andExpect(jsonPath("$.account").value(accountLogin.getAccount()))
						.andExpect(jsonPath("$.password").value(accountLogin.getPassword()))
						.andDo(result -> account = objectMapper.readValue(result.getResponse().getContentAsString(), AccountLogin.class));
	}

	@Test
	@DisplayName("로그인 성공 시 JSON 형태의 데이터가 응답된다.")
	void test1() throws Exception {
		// given
		AccountLogin login = AccountLogin.builder()
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
		AccountLogin login = AccountLogin.builder()
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

		assertThatCode(() -> {
			objectMapper.readValue(contentAsString, AccessToken.class);
		});
	}

	@Test
	@DisplayName("로그인 성공 시 응답되는 accessToken 값이 유효한지 확인한다.")
	void test3() throws Exception {
		// given
		AccountLogin login = AccountLogin.builder()
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
		assertThatCode(() -> {
			AccessToken accessToken = objectMapper.readValue(contentAsString, AccessToken.class);
			jwtTokenizer.parseAccessToken(accessToken.getAccessToken());
		});
	}

	@Test
	@DisplayName("로그인 성공 시 쿠키에 RefreshToken 값이 저장되었는지 확인한다.")
	void test4() throws Exception {
		// given
		AccountLogin login = AccountLogin.builder()
						.account("huning@gmail.com")
						.password("1234")
						.build();

		String json = objectMapper.writeValueAsString(login);

		// when
		mockMvc.perform(post("/auth/login")
										.contentType(APPLICATION_JSON)
										.content(json))
						.andExpect(status().isOk())
						.andExpect(MockMvcResultMatchers.cookie().exists("RefreshToken"))
						.andExpect(result -> {
							String cookieValue = result.getResponse().getCookie("RefreshToken").getValue();
							boolean isNotEmpty = StringUtils.hasText(cookieValue);
							if (!isNotEmpty) {
								throw new AssertionError("Cookie 'RefreshToken' is empty");
							}
						})
						.andDo(print());
	}

	@Test
	@DisplayName("RefreshToken으로 accessToken을 재발급 받는다.")
	void test5() throws Exception {
		// given
		AccountLogin login = AccountLogin.builder()
						.account("huning@gmail.com")
						.password("1234")
						.build();

		String json = objectMapper.writeValueAsString(login);

		// when
		MockHttpServletResponse response1 = mockMvc.perform(post("/auth/login")
										.contentType(APPLICATION_JSON)
										.content(json))
						.andExpect(status().isOk())
						.andDo(print())
						.andReturn().getResponse();


		Cookie refreshToken = response1.getCookie("RefreshToken");
		String contentAsString1 = response1.getContentAsString();
		AccessToken accessToken1 = objectMapper.readValue(contentAsString1, AccessToken.class);

		MockHttpServletResponse response2 = mockMvc.perform(post("/auth/token")
										.contentType(APPLICATION_JSON)
										.header("Authorization", "Bearer " + accessToken1.getAccessToken())
										.cookie(refreshToken))
						.andExpect(status().isOk())
						.andDo(print())
						.andReturn().getResponse();

		String contentAsString2 = response2.getContentAsString();
		AccessToken accessToken2 = objectMapper.readValue(contentAsString2, AccessToken.class);

		// then
		assertThat(accessToken1.getAccessToken()).isNotEqualTo(accessToken2.getAccessToken());
	}

	@Test
	@DisplayName("토큰 생성 시 토큰의 값이 달라진다.")
	void test6() throws Exception {
		String accessToken1 = jwtTokenizer.createAccessToken(1L);
		String accessToken2 = jwtTokenizer.createAccessToken(1L);

		assertThat(accessToken1).isNotEqualTo(accessToken2);
	}

	@Test
	@DisplayName("로그아웃 시 데이터 저장소에서 refresh token이 삭제된다.")
	void test7() throws Exception {
		// given
		AccountLogin login = AccountLogin.builder()
						.account("huning@gmail.com")
						.password("1234")
						.build();

		String json = objectMapper.writeValueAsString(login);

		// when
		MockHttpServletResponse response1 = mockMvc.perform(post("/auth/login")
										.contentType(APPLICATION_JSON)
										.content(json))
						.andExpect(status().isOk())
						.andDo(print())
						.andReturn().getResponse();

		Cookie refreshToken = response1.getCookie("RefreshToken");
		String contentAsString1 = response1.getContentAsString();
		AccessToken accessToken1 = objectMapper.readValue(contentAsString1, AccessToken.class);

		mockMvc.perform(post("/auth/logout")
										.contentType(APPLICATION_JSON)
										.header("Authorization", "Bearer " + accessToken1.getAccessToken())
										.cookie(refreshToken))
						.andExpect(status().isOk())
						.andDo(print());

		// then
		Optional<RefreshToken> findRefreshToken2 = refreshTokenRepository.findRefreshToken(refreshToken.getValue());
		assertThat(findRefreshToken2).isEmpty();
	}
}