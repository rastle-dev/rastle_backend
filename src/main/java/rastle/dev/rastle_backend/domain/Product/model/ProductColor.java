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
public class ProductColor {
    List<Color> productColors;
}
