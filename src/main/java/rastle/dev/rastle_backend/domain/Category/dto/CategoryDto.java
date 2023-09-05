package rastle.dev.rastle_backend.domain.Category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Category.model.Category;

public class CategoryDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCreateRequest {
        String name;

        public Category toEntity() {
            return Category.builder()
                    .name(name).build();
        }
    }
}
