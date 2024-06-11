package rastle.dev.rastle_backend.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

@Data
@Builder
@NoArgsConstructor
@Getter
@Schema(description = "전체 상품 조회 시 리턴 데이터")
public class SimpleProductInfo {
    @Schema(description = "상품 아이디", defaultValue = "0")
    Long id;
    @Schema(description = "상품 명", defaultValue = "멋있는 청바지")
    String name;
    @Schema(description = "상품 가격", defaultValue = "1_000_000")
    int price;
    @Schema(description = "메인 썸네일", defaultValue = "https://aws.~~~~")
    String mainThumbnail;
    @Schema(description = "서브 썸네일", defaultValue = "https://aws.~~~~")
    String subThumbnail;
    @Schema(description = "할인된 가격", defaultValue = "10")
    int discountPrice;
    @Schema(description = "상품 보여질 순서", defaultValue = "100")
    Long displayOrder;
    @Schema(description = "상품 보여질지 여부", defaultValue = "true")
    boolean visible;
    @Schema(description = "상품 카테고리 아이디", defaultValue = "1")
    Long categoryId;
    @Schema(description = "상품 번들 아이디", defaultValue = "1")
    Long bundleId;
    @Schema(description = "상품 이벤트 아이디", defaultValue = "1")
    Long eventId;
    @Schema(description = "상품 이벤트 참여 횟수", defaultValue = "1")
    Long eventApplyCount;
    @Schema(description = "상품 품절 여부", defaultValue = "false")
    Boolean soldOut;

    @QueryProjection
    public SimpleProductInfo( Long id, String name, int price, String mainThumbnail, String subThumbnail, int discountPrice, Long displayOrder, boolean visible, Long categoryId, Long bundleId, Long eventId, Long eventApplyCount, Boolean soldOut) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.mainThumbnail = mainThumbnail;
        this.subThumbnail = subThumbnail;
        this.discountPrice = discountPrice;
        this.displayOrder = displayOrder;
        this.visible = visible;
        this.categoryId = categoryId;
        this.bundleId = bundleId;
        this.eventId = eventId;
        this.eventApplyCount = eventApplyCount;
        this.soldOut = soldOut;
    }


    public static SimpleProductInfo fromEntity(ProductBase productBase) {
        return SimpleProductInfo.builder()
            .id(productBase.getId())
            .name(productBase.getName())
            .price(productBase.getPrice())
            .mainThumbnail(productBase.getMainThumbnailImage())
            .subThumbnail(productBase.getSubThumbnailImage())
            .discountPrice(productBase.getDiscountPrice())
            .displayOrder(productBase.getDisplayOrder())
            .visible(productBase.isVisible())
            .categoryId(productBase.getCategory() == null ? null : productBase.getCategory().getId())
            .bundleId(productBase.getBundle() == null ? null : productBase.getBundle().getId())
            .eventId(productBase.getEvent() == null ? null : productBase.getEvent().getId())
            .eventApplyCount(productBase.getEventApplyCount())
            .soldOut(productBase.getSoldOut())
            .build();
    }

}
