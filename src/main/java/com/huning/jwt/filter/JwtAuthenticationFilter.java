package com.huning.jwt.filter;


import com.huning.jwt.exception.JwtAuthenticationFilterException;
import com.huning.jwt.token.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final AuthenticationManager authenticationManager;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
		try {
			String token = getToken(request);
			if (StringUtils.hasText(token)) {
				getAuthentication(token);
			}
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			throw new JwtAuthenticationFilterException(e);
		}
	}

	private void getAuthentication(String token) {
		JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
		Authentication authenticate = authenticationManager.authenticate(authenticationToken);
		SecurityContextHolder.getContext().setAuthentication(authenticate);
	}

	private String getToken(HttpServletRequest request) {
		String authorization = request.getHeader("Authorization");
		if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer")) {
			String[] arr = authorization.split(" ");
			return arr[1];
		}
		return null;
	}
}
