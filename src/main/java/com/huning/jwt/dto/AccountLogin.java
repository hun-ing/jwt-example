package com.huning.jwt.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountLogin {

	private Long accountId;

	@NotEmpty
	private String account;

	@NotEmpty
	private String password;
}
