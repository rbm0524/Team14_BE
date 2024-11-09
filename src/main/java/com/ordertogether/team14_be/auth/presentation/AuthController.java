package com.ordertogether.team14_be.auth.presentation;

import com.ordertogether.team14_be.auth.application.service.AuthService;
import com.ordertogether.team14_be.auth.application.service.KakaoAuthService;
import com.ordertogether.team14_be.common.web.response.ApiResponse;
import com.ordertogether.team14_be.member.application.dto.MemberInfoRequest;
import com.ordertogether.team14_be.member.application.service.MemberService;
import com.ordertogether.team14_be.member.persistence.entity.Member;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;
	private final KakaoAuthService kakaoAuthService;
	private final String redirectPage;
	private final MemberService memberService;

	public AuthController(
			AuthService authService,
			KakaoAuthService kakaoAuthService,
			MemberService memberService,
			@Value("${FRONT_PAGE_SIGNUP}") String redirectPage) {
		this.authService = authService;
		this.kakaoAuthService = kakaoAuthService;
		this.memberService = memberService;
		this.redirectPage = redirectPage;
	}

	@GetMapping("/login")
	public ResponseEntity<ApiResponse<String>> getToken(
			@RequestHeader("Authorization") String authorizationHeader,
			HttpServletResponse httpServletResponse) {
		String authorizationCode = authorizationHeader.replace("Bearer ", "");
		System.out.println("인가코드:" + authorizationCode);
		String userKakaoEmail = kakaoAuthService.getKakaoUserEmail(authorizationCode);
		System.out.println("이메일:" + userKakaoEmail);
		Optional<Member> existMember = memberService.findMemberByEmail(userKakaoEmail);
		if (existMember.isPresent()) {
			String serviceToken = authService.getServiceToken(userKakaoEmail);

			ResponseCookie cookie =
					ResponseCookie.from("serviceToken", serviceToken)
							.httpOnly(true)
							.secure(true)
							.path("/")
							.sameSite("Strict")
							.build();

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

			return ResponseEntity.ok()
					.headers(headers)
					.body(ApiResponse.with(HttpStatus.OK, "로그인 성공", serviceToken));
		} else {
			String redirectUrl = redirectPage + userKakaoEmail;
			try {
				httpServletResponse.sendRedirect(redirectUrl);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			return ResponseEntity.ok().body(ApiResponse.with(HttpStatus.OK, "리다이렉트", redirectUrl));
		}
	}

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<String>> signUpMember(
			@RequestParam String email, @RequestBody MemberInfoRequest memberInfoRequest) {
		String serviceToken =
				authService.register(
						email, memberInfoRequest.deliveryName(), memberInfoRequest.phoneNumber());

		ResponseCookie cookie =
				ResponseCookie.from("serviceToken", serviceToken)
						.httpOnly(true)
						.secure(true)
						.path("/")
						.sameSite("Strict")
						.build();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

		return ResponseEntity.ok()
				.headers(headers)
				.body(ApiResponse.with(HttpStatus.OK, "회원가입 성공", serviceToken));
	}

	@PostMapping("/logout")
	public void logout(HttpServletResponse response) {
		ResponseCookie deleteCookie =
				ResponseCookie.from("serviceToken", "")
						.maxAge(0)
						.httpOnly(true)
						.secure(true)
						.path("/")
						.sameSite("Strict")
						.build();

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.SET_COOKIE, deleteCookie.toString());
	}
}
