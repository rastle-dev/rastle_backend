package rastle.dev.rastle_backend.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.product.model.ProductColor;
import rastle.dev.rastle_backend.domain.product.model.ProductImage;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductInfo {
    Long id;
    String name;
    int price;
    String mainThumbnail;
    String subThumbnail;
    int discountPrice;
    Long displayOrder;
    boolean visible;
    Long categoryId;
    Long bundleId;
    Long eventId;
    ProductImage productMainImages;
    ProductImage productDetailImages;
    ProductColor productColors;




}
