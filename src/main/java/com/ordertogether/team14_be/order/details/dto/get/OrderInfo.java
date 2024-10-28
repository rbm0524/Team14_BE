package com.ordertogether.team14_be.order.details.dto.get;

public record OrderInfo(
		Long id,
		String category,
		String storeName,
		int minimumOrderAmount,
		String pickUpLocation,
		String deliveryStatus,
		int price,
		boolean isCreator) {}
