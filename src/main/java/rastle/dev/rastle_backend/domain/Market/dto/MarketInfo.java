package rastle.dev.rastle_backend.domain.Market.dto;

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
@Schema(description = "마켓 정보")
public class MarketInfo {
    @Schema(description = "마켓 아이디", defaultValue = "0")
    Long id;
    @Schema(description = "마켓 이름", defaultValue = "첫번째 마켓")
    String name;
    @Schema(description = "마켓 시작 날짜", defaultValue = "2023-01-12T12:00:00")
    LocalDateTime startDate;
    @Schema(description = "마켓 이미지들", defaultValue = "https://aws.~~~, https://aws.~~~, https://aws.~~~")
    String imageUrls;
}
