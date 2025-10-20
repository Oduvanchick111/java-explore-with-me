package ewm.models.category.mapper;

import lombok.experimental.UtilityClass;
import ewm.models.category.dto.CategoryDto;
import ewm.models.category.model.Category;

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
