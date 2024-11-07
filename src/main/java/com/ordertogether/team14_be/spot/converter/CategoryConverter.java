package com.ordertogether.team14_be.spot.converter;

import org.springframework.stereotype.Component;
import com.ordertogether.team14_be.spot.enums.Category;

@Component
public class CategoryConverter extends AbstractCodedEnumConverter<Category, String> {

    public CategoryConverter(Class<Category> clazz) {
        super(clazz); // Category.class를 부모 클래스에 전달
    }
}
