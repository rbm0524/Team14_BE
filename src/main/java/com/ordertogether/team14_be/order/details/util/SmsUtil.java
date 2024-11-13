package com.ordertogether.team14_be.order.details.util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SmsUtil {

	@Value("${COOLSMS_API_KEY}")
	private String apiKey;

	@Value("${COOLSMS_API_SECRET}")
	private String apiSecretKey;

	private DefaultMessageService messageService;

	@PostConstruct
	private void init() {
		this.messageService =
				NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
	}

	// 단일 메시지 발송 예제
	public SingleMessageSentResponse sendOne(String to, String link) {
		Message message = new Message();
		// 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
		message.setFrom("01043064404");
		message.setTo(to);
		message.setText("[여기먹때] 참여하신 함께 주문 배달의 민족 링크입니다. \n" + link);

		SingleMessageSentResponse response =
				this.messageService.sendOne(new SingleMessageSendingRequest(message));
		return response;
	}
}
