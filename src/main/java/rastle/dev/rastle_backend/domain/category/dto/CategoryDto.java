package rastle.dev.rastle_backend.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.category.model.Category;

public class CategoryDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCreateRequest {
        @Schema(description = "생성할 카테고리 이름", defaultValue = "바지")
        String name;

        public Category toEntity() {
            return Category.builder()
                    .name(name).build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryUpdateRequest {
        @Schema(description = "업데이트할 카테고리 이름", defaultValue = "바지")
        String name;
    }
}
