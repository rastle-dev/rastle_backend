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
public class SimpleProductInfo {
    @Schema(description = "상품 아이디")
    Long id;
    @Schema(description = "상품 명")
    String name;
    @Schema(description = "상품 가격")
    int price;
    @Schema(description = "메인 썸네일")
    String mainThumbnail;
    @Schema(description = "서브 썸네일")
    String subThumbnail;
    @Schema(description = "이벤트 상품 여부")
    boolean isEvent;
    @Schema(description = "할인율")
    int discount;
}
