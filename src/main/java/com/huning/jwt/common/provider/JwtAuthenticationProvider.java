package com.huning.jwt.common.provider;

import com.huning.jwt.common.token.JwtAuthenticationToken;
import com.huning.jwt.common.util.AccountDetail;
import com.huning.jwt.common.util.JwtTokenizer;
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
		Long accountId = claims.get("accountId", Long.class);

		AccountDetail loginInfo = new AccountDetail();
		loginInfo.setAccountId(accountId);

		return new JwtAuthenticationToken(loginInfo);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
