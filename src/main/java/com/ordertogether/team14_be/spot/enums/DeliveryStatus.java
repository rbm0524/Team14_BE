package com.ordertogether.team14_be.spot.enums;

import com.ordertogether.team14_be.spot.converter.AbstractCodedEnumConverter;
import com.ordertogether.team14_be.spot.converter.CodedEnum;
import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DeliveryStatus implements CodedEnum<String> {
	DELIVERING("001", "진행중"),
	DELIVERED("002", "완료"),
	;

	private final String code;
	private final String status;

	public static Optional<DeliveryStatus> fromStringToEnum(String status) {
		return Arrays.stream(DeliveryStatus.values()).filter(c -> c.status.equals(status)).findFirst();
	}

	// 한글 설명(String)을 ENUM으로 변환
	@jakarta.persistence.Converter(autoApply = true)
	static class Converter extends AbstractCodedEnumConverter<DeliveryStatus, String> {
		public Converter() {
			super(DeliveryStatus.class);
		}
	}
}
