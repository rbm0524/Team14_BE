package com.ordertogether.team14_be.order.details.dto.get;

import java.time.LocalDateTime;

public record GetParticipantOrderInfoRes(
		String category,
		String storeName,
		int minimumOrderAmount,
		String pickUpLocation,
		String deliveryStatus,
		LocalDateTime orderDate,
		int price) {}
