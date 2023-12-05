package rastle.dev.rastle_backend.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.product.model.Color;
import rastle.dev.rastle_backend.domain.product.model.ProductColor;
import rastle.dev.rastle_backend.domain.product.model.ProductImage;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductInfo {
    Long id;
    String name;
    int price;
    String mainThumbnailImage;
    String subThumbnailImage;
    int discountPrice;
    Long displayOrder;
    boolean visible;
    Long categoryId;
    Long bundleId;
    Long eventId;
    ProductImage productMainImage;
    ProductImage productDetailImage;
    ProductColor productColor;

}
