package rastle.dev.rastle_backend.domain.Product.dto;

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
@Schema(description = "이벤트 상품 조회 시 리턴 데이터")
public class EventProductInfo {
    @Schema(description = "이벤트 아이디", defaultValue = "0")
    Long eventId;
    @Schema(description = "이벤트 이름", defaultValue = "첫번째 이벤트")
    String eventName;
    @Schema(description = "이벤트 시작 날짜", defaultValue = "2023-01-12T12:00:00")
    LocalDateTime startDate;
    @Schema(description = "이벤트 종료 날짜", defaultValue = "2023-01-12T12:00:00")
    LocalDateTime endDate;
    @Schema(description = "이벤트 이미지들", defaultValue = "https://aws.~~~, https://aws.~~~, https://aws.~~~")
    String eventImageUrls;
    @Schema(description = "이벤트 설명", defaultValue = "이벤트 설명입니다")
    String eventDescription;
    @Schema(description = "이벤트의 가시성 여부", defaultValue = "true")
    boolean eventVisible;
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
