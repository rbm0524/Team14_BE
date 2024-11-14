package com.ordertogether.team14_be.order.details.controller;

import com.ordertogether.team14_be.member.persistence.entity.Member;
import com.ordertogether.team14_be.member.presentation.LoginMember;
import com.ordertogether.team14_be.order.details.dto.create.CreateOrderDetailReq;
import com.ordertogether.team14_be.order.details.dto.create.CreateOrderDetailRes;
import com.ordertogether.team14_be.order.details.dto.get.GetCreatorOrderInfoRes;
import com.ordertogether.team14_be.order.details.dto.get.GetOrdersInfoRes;
import com.ordertogether.team14_be.order.details.dto.get.GetParticipantOrderInfoRes;
import com.ordertogether.team14_be.order.details.dto.update.UpdateOrderPriceReq;
import com.ordertogether.team14_be.order.details.service.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderDetailController {

	private final OrderDetailService orderDetailService;

	// Spot만들면서 주문 생성
	// @PostMapping
	public ResponseEntity<CreateOrderDetailRes> createOrderDetail(
			@RequestBody CreateOrderDetailReq createOrderDetailReq) {
		CreateOrderDetailRes createOrderDetailRes =
				orderDetailService.createOrderDetail(createOrderDetailReq);
		return ResponseEntity.ok(createOrderDetailRes);
	}

	@PostMapping
	public ResponseEntity<CreateOrderDetailRes> participantOrder(
			@LoginMember Member member, @RequestBody CreateOrderDetailReq dto) {
		return ResponseEntity.ok(orderDetailService.participantOrder(member, dto));
	}

	@GetMapping
	public ResponseEntity<GetOrdersInfoRes> getOrdersInfo(
			@LoginMember Member member,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "sort") String sort) {
		return ResponseEntity.ok(orderDetailService.getOrdersInfo(member, page, size, sort));
	}

	@GetMapping("/participant")
	public ResponseEntity<GetParticipantOrderInfoRes> getParticipantOrderInfo(
			@LoginMember Member member, @RequestParam(name = "spot-id") Long spotId) {
		return ResponseEntity.ok(orderDetailService.getParticipantOrderInfo(member, spotId));
	}

	@GetMapping("/creator")
	public ResponseEntity<GetCreatorOrderInfoRes> getCreatorOrderInfo(
			@LoginMember Member member, @RequestParam(name = "spot-id") Long spotId) {
		return ResponseEntity.ok(orderDetailService.getCreatorOrderInfo(member, spotId));
	}

	// 가격 수정
	@PutMapping("/price")
	public ResponseEntity<Void> updateOrderPrice(
			@LoginMember Member member, @RequestBody @Valid UpdateOrderPriceReq dto) {
		orderDetailService.updateOrderPrice(member, dto);
		return ResponseEntity.ok().build();
	}
}
