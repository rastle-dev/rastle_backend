package rastle.dev.rastle_backend.domain.Category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryInfo;
import rastle.dev.rastle_backend.domain.Category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(
            "select new rastle.dev.rastle_backend.domain.Category.dto.CategoryInfo(" +
                    "c.id, " +
                    "c.name "+
                    ") from Category c"
    )
    List<CategoryInfo> getCategoryInfos();
}
