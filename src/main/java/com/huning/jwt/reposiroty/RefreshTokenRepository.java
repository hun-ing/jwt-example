package com.huning.jwt.reposiroty;

import com.huning.jwt.common.database.RefreshTokenTable;
import com.huning.jwt.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

	private final RefreshTokenTable refreshTokenTable;

	public void deleteRefreshToken(Long accountId) {
		refreshTokenTable.deleteRefreshToken(accountId);
	}

	public void addRefreshToken(RefreshToken refreshTokenEntity) {
		refreshTokenTable.addRefreshToken(refreshTokenEntity.getAccountId(), refreshTokenEntity);
	}

	public Optional<RefreshToken> findRefreshToken(String refreshToken) {
		return refreshTokenTable.findRefreshToken(refreshToken);
	}
}
