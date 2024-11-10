package com.ordertogether.team14_be.spot.dto.controllerdto;

import java.math.BigDecimal;

public record SpotModifyResponse(
		Long id,
		BigDecimal lat,
		BigDecimal lng,
		String storeName,
		String category,
		Integer minimumOrderAmount,
		String togetherOrderLink,
		String pickUpLocation) {}
