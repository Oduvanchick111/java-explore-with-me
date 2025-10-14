package API.publicAPI.categories;

import models.category.dto.CategoryDto;

import java.util.List;

public interface CategoriesService {

    List<CategoryDto> getCategories(int size, int from);

    CategoryDto getCategoryById(Long categoryId);
}
