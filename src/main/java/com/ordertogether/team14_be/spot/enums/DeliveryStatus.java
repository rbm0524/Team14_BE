package com.ordertogether.team14_be.spot.enums;

import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryStatus {
	DELIVERING("001", "진행중"),
	DELIVERED("002", "완료");

	private final String code;
	private final String status;

	public static Optional<DeliveryStatus> fromStringToEnum(String status) {
		return Arrays.stream(DeliveryStatus.values())
				.filter(c -> c.getStatus().equals(status))
				.findFirst();
	}

	public static Optional<String> fromEnumToString(DeliveryStatus status) {
		return Optional.of(status.getCode())
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배달 상태입니다."))
				.describeConstable();
	}
}
