package com.ordertogether.team14_be.auth.exception;

import com.ordertogether.team14_be.auth.presentation.AuthController;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class LoginExceptionHandler {

	@ExceptionHandler(NotMemberException.class)
	public ResponseEntity<?> handleException(NotMemberException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(Map.of("error", "회원이 아닙니다.", "redirectURL", ex.getRedirectURL()));
	}
}
