package rastle.dev.rastle_backend.domain.Product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductDetailInfo;
import rastle.dev.rastle_backend.domain.Product.model.ProductColor;
import rastle.dev.rastle_backend.domain.Product.model.ProductImage;

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
    String productMainImages;
    String productDetailImages;
    String productColors;

    public ProductDetailInfo toDetailInfo(ProductColor productColor, ProductImage mainImage, ProductImage detailImage) {
        return ProductDetailInfo.builder()
                .id(this.id)
                .name(this.name)
                .price(this.price)
                .mainThumbnail(this.mainThumbnail)
                .subThumbnail(this.subThumbnail)
                .discountPrice(this.discountPrice)
                .displayOrder(this.displayOrder)
                .visible(this.visible)
                .categoryId(this.categoryId)
                .bundleId(this.bundleId)
                .eventId(this.eventId)
                .mainImage(mainImage)
                .detailImage(detailImage)
                .productColor(productColor)
                .build();
    }

}
