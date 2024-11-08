package com.ordertogether.team14_be.spot.converter;

import jakarta.persistence.Converter;
import com.ordertogether.team14_be.spot.enums.Category;

@Converter(autoApply = true)
public class CategoryConverter extends AbstractCodedEnumConverter<Category, String> {

    public CategoryConverter() {
        super(Category.class); // Category.class를 부모 클래스에 전달
    }
}
