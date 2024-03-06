package rastle.dev.rastle_backend.domain.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CategoryInfo {
    @Schema(description = "카테고리 아이디", defaultValue = "1")
    Long id;
    @Schema(description = "카테고리 이름", defaultValue = "바지")
    String name;

}
