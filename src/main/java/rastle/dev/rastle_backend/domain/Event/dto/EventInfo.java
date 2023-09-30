package rastle.dev.rastle_backend.domain.Event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "이벤트 정보")
public class EventInfo {
    @Schema(description = "이벤트 아이디", defaultValue = "0")
    Long id;
    @Schema(description = "이벤트 이름", defaultValue = "첫번째 이벤트")
    String name;
    @Schema(description = "이벤트 시작 날짜", defaultValue = "2023-01-12T12:00:00")
    LocalDateTime startDate;
    @Schema(description = "이벤트 종료 날짜", defaultValue = "2023-01-12T12:00:00")
    LocalDateTime endDate;
    @Schema(description = "이벤트 이미지들", defaultValue = "https://aws.~~~, https://aws.~~~, https://aws.~~~")
    String imageUrls;
    @Schema(description = "이벤트 설명", defaultValue = "이벤트 설명입니다")
    String description;
    @Schema(description = "이벤트의 가시성 여부", defaultValue = "true")
    boolean visible;
}
