package ewm.API.publicAPI.categories;

import lombok.RequiredArgsConstructor;
import ewm.models.apiError.model.NotFoundException;
import ewm.models.category.dto.CategoryDto;
import ewm.models.category.mapper.CategoryMapper;
import ewm.models.category.model.Category;
import ewm.models.category.repo.CategoryRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesPublicServiceImpl implements CategoriesPublicService {

    private final CategoryRepo categoryRepo;

    @Override
    public List<CategoryDto> getCategories(int size, int from) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Category> categories;
        categories = categoryRepo.findAll(pageable).getContent();
        return categories.stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        Category currentCategory = categoryRepo.findById(categoryId).orElseThrow(() -> new NotFoundException("Категория не существует"));
        return CategoryMapper.toCategoryDto(currentCategory);
    }
}
