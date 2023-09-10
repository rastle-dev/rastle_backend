package rastle.dev.rastle_backend.domain.Category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CategoryInfo {
    @Schema(description = "카테고리 아이디", defaultValue = "1")
    Long id;
    @Schema(description = "카테고리 이름", defaultValue = "바지")
    String name;
    @Schema(description = "카테고리 이미지들", defaultValue = "https://aws.~~~, https://aws.~~~, https://aws.~~~")
    String imageUrls;

}
