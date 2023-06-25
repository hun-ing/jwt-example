package com.huning.jwt.common.util;

import java.util.Base64;

public class JwtUtil {
	private byte[] jwtKey;

	public void setJwtKey(String jwtKey) {
		this.jwtKey = Base64.getDecoder().decode(jwtKey);
	}

	public byte[] getJwtKey() {
		return jwtKey;
	}
}
