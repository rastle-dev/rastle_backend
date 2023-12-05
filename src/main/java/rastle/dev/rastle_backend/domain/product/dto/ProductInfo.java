package rastle.dev.rastle_backend.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.product.model.Color;
import rastle.dev.rastle_backend.domain.product.model.ProductColor;
import rastle.dev.rastle_backend.domain.product.model.ProductImage;

import java.util.List;


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
    ProductImage productMainImages;
    ProductImage productDetailImages;
    List<Color> productColors;

    @Builder
    public ProductInfo(Long id, String name, int price, String mainThumbnailImage, String subThumbnailImage, int discountPrice, Long displayOrder, boolean visible, Long categoryId, Long bundleId, Long eventId, ProductImage productMainImages, ProductImage productDetailImages, ProductColor productColors) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.mainThumbnailImage = mainThumbnailImage;
        this.subThumbnailImage = subThumbnailImage;
        this.discountPrice = discountPrice;
        this.displayOrder = displayOrder;
        this.visible = visible;
        this.categoryId = categoryId;
        this.bundleId = bundleId;
        this.eventId = eventId;
        this.productMainImages = productMainImages;
        this.productDetailImages = productDetailImages;
        this.productColors = productColors.getProductColors();
    }
}
