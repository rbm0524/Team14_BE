package com.ordertogether.team14_be.auth.exceptionHandler;

import com.ordertogether.team14_be.auth.exception.NotMemberException;
import com.ordertogether.team14_be.auth.presentation.AuthController;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class LoginExceptionHandler {

	@ExceptionHandler(NotMemberException.class)
	public String handleException(Exception ex) {
		return ex.getMessage();
	}
}
