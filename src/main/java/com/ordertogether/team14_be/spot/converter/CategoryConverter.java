package com.ordertogether.team14_be.spot.converter;

import com.ordertogether.team14_be.spot.enums.Category;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CategoryConverter implements AttributeConverter<Category, String> {

	@Override
	public String convertToDatabaseColumn(
			Category attribute) { // Converts the value stored in the entity attribute into the data
		return attribute.getCode();
	}

	@Override
	public Category convertToEntityAttribute(String dbData) {
		return Category.fromStringToEnum(dbData)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 카테고리입니다."));
	}
}
