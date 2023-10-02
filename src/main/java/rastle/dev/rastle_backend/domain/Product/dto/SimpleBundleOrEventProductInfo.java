package rastle.dev.rastle_backend.domain.Product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Schema(description = "전체 상품 조회 시 리턴 데이터")
public class SimpleBundleOrEventProductInfo {
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
    @Schema(description = "이벤트 상품 여부", defaultValue = "false")
    boolean isEvent;
    @Schema(description = "할인된 가격", defaultValue = "10")
    int discountPrice;
    @Schema(description = "상품 보여질 순서", defaultValue = "100")
    Long displayOrder;
    @Schema(description = "상품 보여질지 여부", defaultValue = "true")
    boolean visible;
    @Schema(description = "상품 카테고리 아이디", defaultValue = "1")
    Long categoryId;
    @Schema(description = "번들 혹은 이벤트 아이디", defaultValue = "2")
    Long eventOrBundleId;
}
