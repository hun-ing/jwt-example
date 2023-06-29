package com.huning.jwt.common.database;

import com.huning.jwt.domain.RefreshToken;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RefreshTokenTable {

	private Map<Long, RefreshToken> refreshTokens = new ConcurrentHashMap<>();

	public void addRefreshToken(Long accountId, RefreshToken refreshToken) {
		refreshTokens.put(accountId, refreshToken);
	}

	public void deleteRefreshToken(Long accountId) {
		refreshTokens.remove(accountId);
	}
}
