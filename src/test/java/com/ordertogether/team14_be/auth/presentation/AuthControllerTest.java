package com.ordertogether.team14_be.auth.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ordertogether.team14_be.auth.application.service.AuthService;
import com.ordertogether.team14_be.auth.application.service.KakaoAuthService;
import com.ordertogether.team14_be.member.application.service.MemberService;
import com.ordertogether.team14_be.member.persistence.entity.Member;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AuthControllerTest {
	private MockMvc mockMvc;

	@Mock private AuthService authService;

	@Mock private KakaoAuthService kakaoAuthService;

	@Mock private MemberService memberService;

	@InjectMocks private AuthController authController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
	}

	@Test
	void 로그인으로_서비스토큰발급() throws Exception {
		String authorizationCode = "testAuthorizationCode";
		String kakaoEmail = "test@kakao.com";
		String serviceToken = "mockedServiceToken";
		Member member = new Member(kakaoEmail);

		when(kakaoAuthService.getKakaoUserEmail(authorizationCode)).thenReturn(kakaoEmail);
		when(memberService.findMemberByEmail(kakaoEmail)).thenReturn(Optional.of(member));
		when(authService.getServiceToken(kakaoEmail)).thenReturn(serviceToken);

		mockMvc
				.perform(get("/api/v1/auth/login").header("Authorization", "Bearer " + authorizationCode))
				.andExpect(status().isOk())
				.andExpect(header().exists(HttpHeaders.SET_COOKIE))
				.andExpect(jsonPath("$.message").value("로그인 성공"))
				.andExpect(jsonPath("$.data").value(serviceToken));
	}

	@Value("${FRONT_PAGE_SIGNUP}")
	private String redirectPage;

	@Test
	void testGetToken_Redirect() throws Exception {
		String authorizationCode = "testAuthorizationCode";
		String kakaoEmail = "test@kakao.com";
		String redirectUrl = redirectPage + kakaoEmail;

		// Mocking the service layer
		when(kakaoAuthService.getKakaoUserEmail(authorizationCode)).thenReturn(kakaoEmail);
		when(memberService.findMemberByEmail(kakaoEmail)).thenReturn(Optional.empty());

		// Perform the GET request
		mockMvc
				.perform(get("/api/v1/auth/login").header("Authorization", "Bearer " + authorizationCode))
				.andExpect(status().isFound())
				.andExpect(header().string(HttpHeaders.LOCATION, redirectUrl));
		;

		// Verify interactions with mocks
		verify(kakaoAuthService).getKakaoUserEmail(authorizationCode);
		verify(memberService).findMemberByEmail(kakaoEmail);
	}
}
