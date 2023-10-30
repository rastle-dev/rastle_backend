package rastle.dev.rastle_backend.domain.Product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "상품 색상 & 사이즈 정보")
public class Color {
    @Schema(description = "상품 색상 정보", defaultValue = "color")
    private String color;
    @Schema(description = "상품 사이즈 정보")
    private List<Size> sizes;
}
