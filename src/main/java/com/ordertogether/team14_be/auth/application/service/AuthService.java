package com.ordertogether.team14_be.auth.application.service;

import com.ordertogether.team14_be.auth.JwtUtil;
import com.ordertogether.team14_be.auth.presentation.KakaoClient;
import com.ordertogether.team14_be.member.application.service.MemberService;
import com.ordertogether.team14_be.member.persistence.entity.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private final KakaoClient kakaoClient;
	private final MemberService memberService;
	private final JwtUtil jwtUtil;
	private final String redirectPage;

	public AuthService(
			KakaoClient kakaoClient,
			MemberService memberService,
			JwtUtil jwtUtil,
			@Value("${FRONT_PAGE_SIGNUP}") String redirectPage) {
		this.kakaoClient = kakaoClient;
		this.memberService = memberService;
		this.jwtUtil = jwtUtil;
		this.redirectPage = redirectPage;
	}

	public String register(String email, String deliveryName, String phoneNumber) {
		Member member = new Member(email, deliveryName, phoneNumber);
		memberService.registerMember(member);
		Long memberId = memberService.getMemberId(email);
		String serviceToken = jwtUtil.generateToken(memberId);
		return serviceToken;
	}

	public String getServiceToken(String email) {
		return jwtUtil.generateToken(memberService.getMemberId(email));
	}
}
