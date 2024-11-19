package com.ordertogether.team14_be.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND) // 404 상태 코드 설정
public class NotMemberException extends RuntimeException {
	private final String redirectURL;

	public NotMemberException(String redirectURL) {
		super("회원이 아닙니다. 회원가입을 진행해주세요.");
		this.redirectURL = redirectURL;
	}
}
