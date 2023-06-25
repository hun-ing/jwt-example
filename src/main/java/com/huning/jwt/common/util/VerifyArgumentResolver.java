package com.huning.jwt.common.util;

import com.huning.jwt.common.token.JwtAuthenticationToken;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


public class VerifyArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(Verify.class) != null
						&& parameter.getParameterType() == AccountDetail.class;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
	                              NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		Authentication authentication;
		try {
			authentication = SecurityContextHolder.getContext().getAuthentication();
		} catch (Exception ex) {
			return null;
		}
		if (authentication == null) {
			return null;
		}

		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
		AccountDetail accountDetail = new AccountDetail();

		Object principal = jwtAuthenticationToken.getPrincipal(); // LoginInfoDto
		if (principal == null)
			return null;

		AccountDetail accountPrincipal = (AccountDetail) principal;
		accountDetail.setUserId(accountPrincipal.getUserId());

		return accountDetail;
	}
}

