package rastle.dev.rastle_backend.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.event.model.Event;
import rastle.dev.rastle_backend.domain.product.model.ProductColor;
import rastle.dev.rastle_backend.domain.product.model.ProductImage;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EventProductDetailInfo {
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
    ProductImage mainImage;
    ProductImage detailImage;
    ProductColor productColor;
    String eventName;
    String eventImageUrls;
    String eventDescription;
    LocalDateTime eventStartDate;
    LocalDateTime eventEndDate;
    Long eventApplyCount;
    String link;

    public static EventProductDetailInfo fromProductInfo(ProductInfo productInfo, Event event) {
        return EventProductDetailInfo.builder()
            .id(productInfo.getId())
            .name(productInfo.getName())
            .price(productInfo.getPrice())
            .mainThumbnailImage(productInfo.getMainThumbnailImage())
            .subThumbnailImage(productInfo.getSubThumbnailImage())
            .discountPrice(productInfo.getDiscountPrice())
            .displayOrder(productInfo.getDisplayOrder())
            .visible(productInfo.isVisible())
            .categoryId(productInfo.getCategoryId())
            .bundleId(productInfo.getBundleId())
            .eventId(productInfo.getEventId())
            .mainImage(productInfo.getMainImage())
            .detailImage(productInfo.getDetailImage())
            .productColor(productInfo.getProductColor())
            .eventName(event.getName())
            .eventImageUrls(event.getImageUrls())
            .eventDescription(event.getDescription())
            .eventStartDate(event.getEventStartDate())
            .eventEndDate(event.getEventEndDate())
            .eventApplyCount(productInfo.getEventApplyCount())
            .link(productInfo.getLink())
            .build();
    }
}
