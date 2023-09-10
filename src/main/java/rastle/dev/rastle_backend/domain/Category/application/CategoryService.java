package rastle.dev.rastle_backend.domain.Category.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto.CategoryCreateRequest;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryInfo;
import rastle.dev.rastle_backend.domain.Category.model.Category;
import rastle.dev.rastle_backend.domain.Category.repository.CategoryRepository;
import rastle.dev.rastle_backend.global.component.S3Component;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;



    @Transactional(readOnly = true)
    public List<CategoryInfo> getCategories() {
        return categoryRepository.getCategoryInfos();
    }
}
