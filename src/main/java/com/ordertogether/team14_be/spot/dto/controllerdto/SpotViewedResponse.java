package com.ordertogether.team14_be.spot.dto.controllerdto;

import java.time.LocalTime;

public record SpotViewedResponse(
		Long id,
		String category,
		String storeName,
		Integer minimumOrderAmount,
		String pickUpLocation,
		LocalTime deadlineTime) {}
