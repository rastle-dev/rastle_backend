package rastle.dev.rastle_backend.domain.Product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "상품 사이즈 정보")
public class ProductSize {
    @Schema(description = "상품 사이즈 정보", defaultValue = "size")
    private String size;
    @Schema(description = "상품 수량 정보", defaultValue = "10")
    private Integer count;
}
