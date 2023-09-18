package rastle.dev.rastle_backend.domain.Event.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EventDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventCreateRequest {
        @Schema(description = "이벤트 이름", defaultValue = "첫번째 이벤트")
        String name;
        @Schema(description = "이벤트 시작일", defaultValue = "2023-08-23")
        String startDate;
        @Schema(description = "이벤트 종료일", defaultValue = "2023-09-23")
        String endDate;
        @Schema(description = "이벤트 시작 시간", defaultValue = "12")
        String startHour;
        @Schema(description = "이벤트 시작 분", defaultValue = "0")
        String startMinute;
        @Schema(description = "이벤트 시작 초", defaultValue = "0")
        String startSecond;
        @Schema(description = "이벤트 종료 시간", defaultValue = "23")
        String endHour;
        @Schema(description = "이벤트 종료 분", defaultValue = "59")
        String endMinute;
        @Schema(description = "이벤트 종료 초", defaultValue = "59")
        String endSecond;
        @Schema(description = "이벤트 설명", defaultValue = "이벤트 설명입니다")
        String description;
    }
}
