package com.huning.jwt.reposiroty;

import com.huning.jwt.common.database.RefreshTokenTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

	private final RefreshTokenTable refreshTokenTable;

	public void deleteRefreshToken(Long accountId) {
		refreshTokenTable.deleteRefreshToken(accountId);
	}
}
