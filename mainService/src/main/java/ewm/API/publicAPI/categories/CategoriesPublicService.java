package ewm.API.publicAPI.categories;

import ewm.models.category.dto.CategoryDto;

import java.util.List;

public interface CategoriesPublicService {

    List<CategoryDto> getCategories(int size, int from);

    CategoryDto getCategoryById(Long categoryId);
}
