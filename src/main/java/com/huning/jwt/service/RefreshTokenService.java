package com.huning.jwt.service;

import com.huning.jwt.reposiroty.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	public void deleteRefreshToken(Long accountId) {
		refreshTokenRepository.deleteRefreshToken(accountId);
	}
}
