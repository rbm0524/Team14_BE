package com.ordertogether.team14_be.spot.converter;

import jakarta.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCodedEnumConverter<T extends Enum<T> & CodedEnum<E>, E>
		implements AttributeConverter<T, E> {

	private final Class<T> clazz;

	protected AbstractCodedEnumConverter(Class<T> clazz) {
		this.clazz = clazz;
	}

	@Override
	// Entity의 enum값을 DB에 변환하는 방식을 정의
	public E convertToDatabaseColumn(
			T attribute) { // Converts the value stored in the entity attribute into the data
		// representation to be stored in the database.
		if (Objects.isNull(attribute)) {
			return null;
		}
		return attribute.getCode(); // 코드 저장 ex) KOREAN_STEW -> "002"
	}

	// DB값을 Entity의 enum값으로 변환하는 방식을 정의
	@Override
	public T convertToEntityAttribute(E dbData) {
		if (Objects.isNull(dbData)) {
			return null;
		}

		// 필터링된 결과가 여러 개일 경우 IllegalArgumentException을 던집니다.
		var matchingEnums =
				Arrays.stream(clazz.getEnumConstants()).filter(e -> e.getCode().equals(dbData)).toList();

		if (matchingEnums.size() != 1) {
			log.info("Failed to convert to entity attribute. dbData: {}, clazz: {}", dbData, clazz);
		}
		return matchingEnums.get(0);
	}
}
