package ewm.API.adminAPI.categories;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ewm.models.category.dto.CategoryDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {

    private final AdminCategoriesService adminCategoriesService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        return adminCategoriesService.createCategory(categoryDto);
    }

    @DeleteMapping("/{catId}")
    void deleteCategoryById(@PathVariable Long catId) {
        adminCategoriesService.deleteCategoryById(catId);
    }

    @PatchMapping("/{catId}")
    CategoryDto updateCategory(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        return adminCategoriesService.updateCategory(catId, categoryDto);
    }
    @GetMapping("/test-error")
    public String testError() {
        throw new IllegalArgumentException("Test error handling");
    }
}
