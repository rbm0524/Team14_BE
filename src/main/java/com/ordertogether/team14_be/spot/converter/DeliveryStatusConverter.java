package com.ordertogether.team14_be.spot.converter;

import com.ordertogether.team14_be.spot.enums.DeliveryStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class DeliveryStatusConverter implements AttributeConverter<DeliveryStatus, String> {

	@Override
	public String convertToDatabaseColumn(DeliveryStatus attribute) {
		return attribute.getCode();
	}

	@Override
	public DeliveryStatus convertToEntityAttribute(String dbData) {
		return DeliveryStatus.fromStringToEnum(dbData)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 배달 상태입니다."));
	}
}
