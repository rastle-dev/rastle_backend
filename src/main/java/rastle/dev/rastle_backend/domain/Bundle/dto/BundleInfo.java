package rastle.dev.rastle_backend.domain.Bundle.dto;

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
@Schema(description = "상품 세트 정보")
public class BundleInfo {
    @Schema(description = "상품 세트 아이디", defaultValue = "0")
    Long id;
    @Schema(description = "상품 세트 이름", defaultValue = "첫번째 상품 세트")
    String name;
    @Schema(description = "상품 세트 이미지들", defaultValue = "https://aws.~~~, https://aws.~~~, https://aws.~~~")
    String imageUrls;
    @Schema(description = "상품 세트 설명", defaultValue = "상품 세트 설명입니다")
    String description;
    @Schema(description = "상품 세트 판매 시작 시간", defaultValue = "2023-09-10T12:00:00")
    LocalDateTime saleStartTime;
    @Schema(description = "상품 세트의 가시성 여부", defaultValue = "true")
    boolean visible;
}
