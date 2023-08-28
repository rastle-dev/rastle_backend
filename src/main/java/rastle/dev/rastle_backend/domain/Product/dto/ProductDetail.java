package rastle.dev.rastle_backend.domain.Product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Product.model.Color;
import rastle.dev.rastle_backend.domain.Product.model.ProductImage;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Schema(description = "상품 상세 조회시 리턴 데이터")
public class ProductDetail {
    SimpleProductInfo info;
    ProductImage mainImages;
    ProductImage contentImages;

}
