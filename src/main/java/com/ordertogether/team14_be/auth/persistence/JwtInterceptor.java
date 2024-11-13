package com.ordertogether.team14_be.auth.persistence;

import com.ordertogether.team14_be.member.application.exception.NotFoundMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {
	private final JwtUtil jwtUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("JwtInterceptor 실행");
		if (HttpMethod.OPTIONS.matches(request.getMethod())) {
			log.info("OPTIONS 요청");
			return true;
		}

		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		String token = authorization.replaceAll("Bearer ", "");

		if (token != null && token.length() > 10) {
			log.debug("토큰 상태:: " + token);

			if (jwtUtil.vaildToken(token)) {
				String memberIdString = jwtUtil.decodeJwt(token).getSubject();
				Long memberId = Long.parseLong(memberIdString);

				request.setAttribute("memberId", memberId);
				return true;
			}
		} else {
			throw new NotFoundMember();
		}
		return false;
	}
}
