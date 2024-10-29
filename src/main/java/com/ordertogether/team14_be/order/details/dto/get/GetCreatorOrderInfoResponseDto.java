package com.ordertogether.team14_be.order.details.dto.get;

import java.util.List;

public record GetCreatorOrderInfoResponseDto(
		String category,
		String storeName,
		int minimumOrderAmount,
		String pickUpLocation,
		boolean deliveryStatus,
		List<MemberBriefInfo> memberInfo) {}
