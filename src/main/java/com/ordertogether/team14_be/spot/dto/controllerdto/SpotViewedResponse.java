package com.ordertogether.team14_be.spot.dto.controllerdto;

import java.math.BigDecimal;
import java.time.LocalTime;

public record SpotViewedResponse(
		Long id,
		BigDecimal lat,
		BigDecimal lng,
		String category,
		String storeName,
		Integer minimumOrderAmount,
		String togetherOrderLink,
		String pickUpLocation,
		LocalTime deadlineTime) {}
