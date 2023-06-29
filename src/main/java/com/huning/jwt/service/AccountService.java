package com.huning.jwt.service;

import com.huning.jwt.dto.AccountLogin;
import com.huning.jwt.reposiroty.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

	private final AccountRepository accountRepository;

	public Long addAccount(AccountLogin accountLogin) {
		return accountRepository.addAccount(accountLogin);
	}

	public AccountLogin getAccountByAccount(String account) {
		return accountRepository.getAccountByAccount(account);
	}

	public AccountLogin findByAccountId(Long accountId) {
		return accountRepository.findByAccountId(accountId);
	}

	public void deleteAccount(Long accountId) {
		accountRepository.deleteAccount(accountId);
	}
}
