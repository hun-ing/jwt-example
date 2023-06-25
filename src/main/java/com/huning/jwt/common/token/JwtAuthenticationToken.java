package com.huning.jwt.common.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
	private String token;
	private Object principal;
	private Object credentials;

	public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities,
	                              Object principal, Object credentials) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		this.setAuthenticated(true);
	}

	public JwtAuthenticationToken(Object principal, Object credentials) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(false);
	}

	public JwtAuthenticationToken(Object principal) {
		super(null);
		this.principal = principal;
		this.credentials = null;
		setAuthenticated(true);
	}

	public JwtAuthenticationToken(String token) {
		super(null);
		this.token = token;
		this.setAuthenticated(false);
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	} // 기존 코드를 수정
}