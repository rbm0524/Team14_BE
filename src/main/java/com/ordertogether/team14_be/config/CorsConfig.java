package com.ordertogether.team14_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping("/**")
				.allowedOrigins("http://localhost:3000", "https://team14-fe-livid.vercel.app/")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				.allowedHeaders("Authorization", "Content-Type")
				.exposedHeaders("Custom-Header")
				.allowCredentials(true) // 인증 정보를 보내는 요청을 허용 (쿠키 등)
				.maxAge(3600);
	}
}
