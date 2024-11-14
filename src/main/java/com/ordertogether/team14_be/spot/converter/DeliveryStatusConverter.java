package com.ordertogether.team14_be.spot.converter;

import com.ordertogether.team14_be.spot.enums.DeliveryStatus;
import jakarta.persistence.Converter;

@Converter
public class DeliveryStatusConverter extends AbstractCodedEnumConverter<DeliveryStatus, String> {

	public DeliveryStatusConverter() {
		super(DeliveryStatus.class);
	}
}
