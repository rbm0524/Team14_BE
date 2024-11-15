package com.ordertogether.team14_be.spot.converter;

import com.ordertogether.team14_be.spot.enums.DeliveryStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DeliveryStatusConverter implements AttributeConverter<DeliveryStatus, String> {

	@Override
	public String convertToDatabaseColumn(DeliveryStatus attribute) {
		return (attribute == null) ? null : attribute.getCode();
	}

	@Override
	public DeliveryStatus convertToEntityAttribute(String dbData) {
		return (dbData == null)
				? null
				: DeliveryStatus.fromStringToEnum(dbData)
						.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
	}
}
