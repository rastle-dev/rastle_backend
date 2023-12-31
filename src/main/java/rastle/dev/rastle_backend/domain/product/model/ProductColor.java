package rastle.dev.rastle_backend.domain.product.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rastle.dev.rastle_backend.global.converter.JsonConverter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "상품 색상 & 사이즈 정보")
public class ProductColor {
    List<Color> productColors;

    public static class ProductColorConverter extends JsonConverter<ProductColor> {

    }
}
