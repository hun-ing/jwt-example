package com.huning.jwt.provider;

import com.huning.jwt.token.JwtAuthenticationToken;
import com.huning.jwt.util.JwtTokenizer;
import com.huning.jwt.util.LoginInfoDto;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JwtTokenizer jwtTokenizer;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
		Claims claims = jwtTokenizer.parseAccessToken(authenticationToken.getToken());
		Long userId = claims.get("userId", Long.class);

		LoginInfoDto loginInfo = new LoginInfoDto();
		loginInfo.setUserId(userId);

		return new JwtAuthenticationToken(loginInfo);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
