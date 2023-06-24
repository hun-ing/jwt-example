package com.huning.jwt.config;

import com.huning.jwt.util.JwtUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.config")
public class AppConfig {

	public JwtUtil jwtUtil;
}
