package rastle.dev.rastle_backend.domain.category.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.category.dto.CategoryInfo;
import rastle.dev.rastle_backend.domain.category.repository.mysql.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryInfo> getCategories() {
        return categoryRepository.getCategoryInfos();
    }
}
