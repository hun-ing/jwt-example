package com.huning.jwt.common.database;

import com.huning.jwt.domain.RefreshToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RefreshTokenTable {

	private Map<Long, RefreshToken> refreshTokens = new ConcurrentHashMap<>();

	public void addRefreshToken(Long accountId, RefreshToken refreshToken) {
		refreshTokens.put(accountId, refreshToken);
	}

	public void deleteRefreshToken(Long accountId) {
		refreshTokens.remove(accountId);
	}

	public Optional<RefreshToken> findRefreshToken(String refreshToken) {
		return refreshTokens.values().stream().filter(refreshTokenObj -> refreshTokenObj.getRefreshToken().equals(refreshToken)).findFirst();
	}
}
