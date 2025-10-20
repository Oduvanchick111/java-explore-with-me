package ewm.API.publicAPI.categories;

import lombok.RequiredArgsConstructor;
import ewm.models.category.dto.CategoryDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesPublicController {

    private final CategoriesPublicService categoriesPublicService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam (defaultValue = "10") int size, @RequestParam (defaultValue = "0") int from) {
       return categoriesPublicService.getCategories(size, from);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable Long categoryId) {
        return categoriesPublicService.getCategoryById(categoryId);
    }
}
