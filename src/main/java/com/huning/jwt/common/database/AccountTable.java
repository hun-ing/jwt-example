package com.huning.jwt.common.database;

import com.huning.jwt.dto.AccountLogin;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountTable {

	private Long accountIdCount = 0L;
	private Map<Long, AccountLogin> accounts = new ConcurrentHashMap<>();

	public Optional<AccountLogin> getAccount(Long accountId) {
		return Optional.ofNullable(accounts.get(accountId));
	}

	public Long setAccount(AccountLogin accountLogin) {
		accountIdCount++;
		accounts.put(accountIdCount, accountLogin);
		return accountIdCount;
	}

	public Optional<AccountLogin> getAccountByAccount(String account) {
		return accounts.values().stream().filter(accountLogin -> account.equals(accountLogin.getAccount())).findFirst();
	}

	public void deleteAccount(Long accountId) {
		accounts.remove(accountId);
	}
}
