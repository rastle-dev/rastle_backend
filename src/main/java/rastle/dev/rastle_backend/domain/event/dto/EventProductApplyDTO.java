package rastle.dev.rastle_backend.domain.event.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "이벤트 응모 신청 DTO")
public class EventProductApplyDTO {
    @Schema(description = "이벤트 응모 인스타그램 아이디", type = "string", format = "instagramId", example = "rastle_fashion")
    private String instagramId;
    @Schema(description = "이벤트 응모 전화번호", type = "string", format = "phoneNumber", example = "01012345678")
    private String eventPhoneNumber;
    @Schema(description = "이벤트 상품 아이디", type = "Long", format = "eventProductId", example = "1")
    private Long eventProductId;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "이벤트 응모 내역 정보 DTO")
    public static class EventProductApplyInfoDTO {
        @Schema(description = "이벤트 응모 제품명", type = "string", format = "name", example = "청바지")
        private String eventProductName;
        @Schema(description = "이벤트 응모 날짜", type = "LocalDateTime", format = "applyDate", example = "2021-08-01T00:00:00")
        private LocalDateTime eventApplyDate;
        @Schema(description = "이벤트 응모 인스타그램 아이디", type = "string", format = "instagramId", example = "rastle_fashion")
        private String instagramId;
        @Schema(description = "이벤트 응모 전화번호", type = "string", format = "phoneNumber", example = "01012345678")
        private String eventPhoneNumber;
    }
}
