package ewm.API.adminAPI.categories;


import ewm.models.apiError.model.ConflictException;
import ewm.models.apiError.model.NotFoundException;
import ewm.models.category.dto.CategoryDto;
import ewm.models.category.mapper.CategoryMapper;
import ewm.models.category.model.Category;
import ewm.models.category.repo.CategoryRepo;
import ewm.models.event.repo.EventRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class AdminCategoriesServiceImpl implements AdminCategoriesService {

    private final CategoryRepo categoryRepo;
    private final EventRepo eventRepo;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if (categoryRepo.existsByName(categoryDto.getName())) {
            throw new ConflictException("Категория с именем '" + categoryDto.getName() + "' уже существует");
        }
        Category category = CategoryMapper.toCategoryEntity(categoryDto);
        categoryRepo.save(category);
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + categoryId + " не найдена"));
        if (eventRepo.existsByCategoryId(categoryId)) {
            throw new ConflictException("Существуют события, связанные с категорией");
        }
        categoryRepo.deleteById(categoryId);
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, CategoryDto categoryDto) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + categoryId + " не найдена"));
        if (categoryDto.getName() != null && !categoryDto.getName().equals(category.getName()) && categoryRepo.existsByName(categoryDto.getName())) {
            throw new ConflictException("Категория с именем '" + categoryDto.getName() + "' уже существует");
        }
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        Category updatedCategory = categoryRepo.save(category);
        return CategoryMapper.toCategoryDto(updatedCategory);
    }

}
