package rastle.dev.rastle_backend.domain.Category.api;

import com.amazonaws.Response;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.Category.application.CategoryService;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto.CategoryCreateRequest;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import java.util.List;

@Tag(name = "카테고리 관련 API", description = "카테고리 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("")
    public ResponseEntity<ServerResponse<?>> createCategory(@RequestBody CategoryCreateRequest createRequest) {
        return ResponseEntity.ok(new ServerResponse<>(categoryService.createCategory(createRequest)));
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<ServerResponse<?>> addImagesToCategory(@PathVariable("id") Long id, @RequestParam("images") List<MultipartFile> images) {
        return ResponseEntity.ok(new ServerResponse<>(categoryService.uploadImages(id, images)));
    }

    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getCategories() {
        return ResponseEntity.ok(new ServerResponse<>(categoryService.getCategories()));
    }
}

