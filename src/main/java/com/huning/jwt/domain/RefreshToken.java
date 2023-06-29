package com.huning.jwt.domain;

import lombok.Data;


@Data
public class RefreshToken {
    private Long id;
    private Long accountId;
    private String refreshToken;
}
