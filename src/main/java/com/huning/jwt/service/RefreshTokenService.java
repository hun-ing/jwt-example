package com.huning.jwt.service;

import com.huning.jwt.domain.RefreshToken;
import com.huning.jwt.reposiroty.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;

	public void deleteRefreshToken(String refreshToken) {
		refreshTokenRepository.deleteRefreshToken(refreshToken);
	}

	public void addRefreshToken(RefreshToken refreshTokenEntity) {
		refreshTokenRepository.addRefreshToken(refreshTokenEntity);
	}

	public Optional<RefreshToken> findRefreshToken(String refreshToken) {
		return refreshTokenRepository.findRefreshToken(refreshToken);
	}
}
