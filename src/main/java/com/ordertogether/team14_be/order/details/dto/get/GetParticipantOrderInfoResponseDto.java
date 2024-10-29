package com.ordertogether.team14_be.order.details.dto.get;

public record GetParticipantOrderInfoResponseDto(
		String category,
		String storeName,
		int minimumOrderAmount,
		String pickUpLocation,
		String deliveryStatus,
		int price) {}
