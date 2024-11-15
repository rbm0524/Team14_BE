package com.ordertogether.team14_be.config;

import com.ordertogether.team14_be.auth.persistence.JwtInterceptor;
import com.ordertogether.team14_be.member.application.LoginMemberArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
	private final LoginMemberArgumentResolver loginMemberArgumentResolver;
	private final JwtInterceptor jwtInterceptor;

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(loginMemberArgumentResolver);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
				.addInterceptor(jwtInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns(
						// Auth 관련 경로
						"/signup",
						"/api/v1/auth/signup",
						"/api/v1/auth/login",
						"/api/v1/spot/**",

						// Swagger UI 관련 경로
						"/swagger-ui.html",
						"/swagger-ui/**",
						"/v3/api-docs/**",
						"/swagger-resources/**",
						"/webjars/**",
						"/swagger/**",
						"/favicon.ico",
						"/error");
	}
}
