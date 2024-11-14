package com.ordertogether.team14_be.order.details.controller;

import com.ordertogether.team14_be.member.persistence.entity.Member;
import com.ordertogether.team14_be.member.presentation.LoginMember;
import com.ordertogether.team14_be.order.details.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/spot/sms/{spotId}")
public class SmsController {

	private final SmsService smsService;

	@GetMapping
	public ResponseEntity<Void> getOrdersInfo(@LoginMember Member member, @PathVariable Long spotId) {
		smsService.sendLinkToParticipant(member, spotId);
		return ResponseEntity.ok().build();
	}
}
