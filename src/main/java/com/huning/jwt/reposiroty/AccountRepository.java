package com.huning.jwt.reposiroty;

import com.huning.jwt.common.database.AccountTable;
import com.huning.jwt.dto.AccountLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

	private final AccountTable accountTable;

	public Long addAccount(AccountLogin accountLogin) {
		return accountTable.setAccount(accountLogin);
	}

	public AccountLogin getAccountByAccount(String account) {
		return accountTable.getAccountByAccount(account).orElseThrow(() -> new UsernameNotFoundException("해당 계정을 찾을 수 없습니다."));
	}

	public AccountLogin findByAccountId(Long accountId) {
		return accountTable.getAccount(accountId).orElseThrow(() -> new IllegalArgumentException("계정을 찾을 수 없습니다."));
	}

	public void deleteAccount(Long accountId) {
		accountTable.deleteAccount(accountId);
	}
}
