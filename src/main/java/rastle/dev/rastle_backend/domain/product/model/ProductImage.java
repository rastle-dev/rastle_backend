package rastle.dev.rastle_backend.domain.product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.global.converter.JsonConverter;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "상품 이미지 정보")
public class ProductImage {
    @Schema(description = "이미지 경로들", defaultValue = "url list")
    List<String> imageUrls;

    public static class ProductImageConverter extends JsonConverter<ProductImage> {
    }
}
