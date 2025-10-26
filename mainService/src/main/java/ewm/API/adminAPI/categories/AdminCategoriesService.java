package ewm.API.adminAPI.categories;

import ewm.models.category.dto.CategoryDto;

public interface AdminCategoriesService {
    CategoryDto createCategory(CategoryDto categoryDto);

    void deleteCategoryById(Long categoryId);

    CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto);
}
