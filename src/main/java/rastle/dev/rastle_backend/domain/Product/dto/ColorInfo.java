package rastle.dev.rastle_backend.domain.Product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Schema(description = "상품 색상, 사이즈 정보")
public class ColorInfo {
    @Schema(description = "색상 정보")
    String color;
    @Schema(description = "사이즈 정보")
    String size;
    @Schema(description = "수량 정보")
    int count;
}
