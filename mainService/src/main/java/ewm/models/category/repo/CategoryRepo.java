package ewm.models.category.repo;

import ewm.models.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
