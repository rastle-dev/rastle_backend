package rastle.dev.rastle_backend.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Schema(description = "세트 상품 조회 시 리턴 데이터")
public class BundleProductInfo {
    @Schema(description = "상품 세트 아이디", defaultValue = "0")
    Long bundleId;
    @Schema(description = "상품 세트 이름", defaultValue = "첫번째 상품 세트")
    String bundleName;
    @Schema(description = "상품 세트 이미지들", defaultValue = "https://aws.~~~, https://aws.~~~, https://aws.~~~")
    String bundleImageUrls;
    @Schema(description = "상품 세트 설명", defaultValue = "상품 세트 설명입니다")
    String bundleDescription;
    @Schema(description = "상품 세트 판매 시작 시간", defaultValue = "2023-09-10T12:00:00")
    LocalDateTime saleStartTime;
    @Schema(description = "상품 세트의 가시성 여부", defaultValue = "true")
    boolean bundleVisible;
    @Schema(description = "상품 아이디", defaultValue = "0")
    Long productId;
    @Schema(description = "상품 명", defaultValue = "멋있는 청바지")
    String productName;
    @Schema(description = "상품 가격", defaultValue = "1_000_000")
    int price;
    @Schema(description = "메인 썸네일", defaultValue = "https://aws.~~~~")
    String mainThumbnail;
    @Schema(description = "서브 썸네일", defaultValue = "https://aws.~~~~")
    String subThumbnail;
    @Schema(description = "할인가격", defaultValue = "90000")
    int discountPrice;
    @Schema(description = "상품 보여질 순서", defaultValue = "100")
    Long displayOrder;
    @Schema(description = "상품 보여질지 여부", defaultValue = "true")
    boolean productVisible;

}
