package com.ordertogether.team14_be.auth.application.service;

import com.ordertogether.team14_be.auth.application.dto.KakaoUserInfo;
import com.ordertogether.team14_be.auth.persistence.JwtUtil;
import com.ordertogether.team14_be.auth.presentation.KakaoClient;
import com.ordertogether.team14_be.member.application.service.MemberService;
import com.ordertogether.team14_be.member.persistence.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KakaoAuthService {
	private final KakaoClient kakaoClient;
	private final MemberService memberService;
	private final JwtUtil jwtUtil;
	private static final String LOGIN_PLATFORM = "KAKAO";

	public String getKakaoUserEmail(String authorizationCode) {
		String kakaoToken = kakaoClient.getAccessToken(authorizationCode);
		KakaoUserInfo kakaoUserInfo = kakaoClient.getUserInfo((kakaoToken));
		String userKakaoEmail = kakaoUserInfo.kakaoAccount().email();
		return userKakaoEmail;
	}

	public String register(String email, String deliveryName, String phoneNumber) {
		Member member = new Member(email, 0, deliveryName, phoneNumber, LOGIN_PLATFORM);
		memberService.registerMember(member);
		Long memberId = memberService.getMemberId(email);
		String serviceToken = jwtUtil.generateToken(memberId);
		return serviceToken;
	}
}
