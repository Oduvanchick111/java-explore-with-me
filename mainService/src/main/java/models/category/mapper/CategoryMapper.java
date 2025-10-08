package models.category.mapper;

import lombok.experimental.UtilityClass;
import models.category.dto.CategoryDto;
import models.category.model.Category;

@UtilityClass
public class CategoryMapper {

    public Category toCategoryEntity(CategoryDto categoryDto){
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
