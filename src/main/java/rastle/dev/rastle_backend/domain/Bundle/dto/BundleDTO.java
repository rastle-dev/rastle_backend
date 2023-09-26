package rastle.dev.rastle_backend.domain.Bundle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BundleDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BundleCreateRequest {
        @Schema(description = "마켓 이름", defaultValue = "첫번째 마켓")
        String name;
        @Schema(description = "마켓 시작일", defaultValue = "2023-08-23")
        String startDate;
        @Schema(description = "마켓 시작 시간", defaultValue = "12")
        String startHour;
        @Schema(description = "마켓 시작 분", defaultValue = "0")
        String startMinute;
        @Schema(description = "마켓 시작 초", defaultValue = "0")
        String startSecond;
        @Schema(description = "마켓 설명", defaultValue = "마켓 설명입니다")
        String description;
        @Schema(description = "상품 세트가 보여질지 여부", defaultValue = "true")
        boolean visible;
    }
}
