package rastle.dev.rastle_backend.domain.Product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Schema(description = "상품 이미지 추가시 리턴 데이터")
public class ProductImageInfo {
    @Schema(description = "상품 아이디", defaultValue = "0")
    Long productBaseId;
    @Schema(description = "추가된 이미지 url 리스트", defaultValue = "list of image urls")
    List<String> imageUrls;
}
