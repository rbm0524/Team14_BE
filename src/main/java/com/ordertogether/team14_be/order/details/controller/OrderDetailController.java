package com.ordertogether.team14_be.order.details.controller;

import com.ordertogether.team14_be.member.persistence.entity.Member;
import com.ordertogether.team14_be.member.presentation.LoginMember;
import com.ordertogether.team14_be.order.details.dto.create.CreateOrderDetailRequestDto;
import com.ordertogether.team14_be.order.details.dto.create.CreateOrderDetailResponseDto;
import com.ordertogether.team14_be.order.details.dto.get.GetOrdersInfoRequestDto;
import com.ordertogether.team14_be.order.details.dto.get.GetOrdersInfoResponseDto;
import com.ordertogether.team14_be.order.details.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderDetailController {

	private final OrderDetailService orderDetailService;

	// 주문 생성
	@PostMapping
	public ResponseEntity<CreateOrderDetailResponseDto> createOrderDetail(
			@RequestBody CreateOrderDetailRequestDto createOrderDetailRequestDto) {
		CreateOrderDetailResponseDto createOrderDetailResponseDto =
				orderDetailService.createOrderDetail(createOrderDetailRequestDto);
		return ResponseEntity.ok(createOrderDetailResponseDto);
	}

	@GetMapping
	public ResponseEntity<GetOrdersInfoResponseDto> getOrdersInfo(
			@LoginMember Member member, @ModelAttribute @Valid GetOrdersInfoRequestDto dto) {
		return ResponseEntity.ok(orderDetailService.getOrdersInfo(member, dto));
	}
}
