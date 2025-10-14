package API.publicAPI.categories;

import lombok.RequiredArgsConstructor;
import models.apiError.model.NotFoundException;
import models.category.dto.CategoryDto;
import models.category.mapper.CategoryMapper;
import models.category.model.Category;
import models.category.repo.CategoryRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {

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
