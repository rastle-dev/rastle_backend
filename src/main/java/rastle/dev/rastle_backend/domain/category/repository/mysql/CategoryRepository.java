package rastle.dev.rastle_backend.domain.category.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rastle.dev.rastle_backend.domain.category.dto.CategoryInfo;
import rastle.dev.rastle_backend.domain.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(
        "select new rastle.dev.rastle_backend.domain.category.dto.CategoryInfo(" +
            "c.id, " +
            "c.name " +
            ") from Category c"
    )
    List<CategoryInfo> getCategoryInfos();
}
