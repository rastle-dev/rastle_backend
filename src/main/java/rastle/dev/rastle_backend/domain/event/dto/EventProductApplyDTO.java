package rastle.dev.rastle_backend.domain.event.dto;

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
    @Schema(description = "회원 이벤트 응모 내역 정보 DTO")
    public static class MemberEventApplyHistoryDTO {
        @Schema(description = "이벤트 응모 상품 아이디", type = "Long", format = "eventProductId", example = "1")
        private Long eventProductId;
        @Schema(description = "이벤트 응모 제품명", type = "string", format = "name", example = "청바지")
        private String eventProductName;
        @Schema(description = "이벤트 응모 제품 썸네일 이미지", type = "string", format = "mainThumbnailImage", example = "https://aws.~~~~")
        private String eventProductMainThumbnailImage;
        @Schema(description = "이벤트 응모 날짜", type = "LocalDateTime", format = "applyDate", example = "2021-08-01T00:00:00")
        private LocalDateTime eventApplyDate;
        @Schema(description = "이벤트 응모 인스타그램 아이디", type = "string", format = "instagramId", example = "rastle_fashion")
        private String instagramId;
        @Schema(description = "이벤트 응모 전화번호", type = "string", format = "phoneNumber", example = "01012345678")
        private String eventPhoneNumber;
        private LocalDateTime startDate;
        private LocalDateTime endDate;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Schema(description = "제품 이벤트 응모 내역 정보 DTO")
    public static class ProductEventApplyHistoryDTO {
        @Schema(description = "회원 이름", type = "string", format = "name", example = "홍길동")
        private String memberName;
        @Schema(description = "이벤트 응모 날짜", type = "LocalDateTime", format = "applyDate", example = "2021-08-01T00:00:00")
        private LocalDateTime eventApplyDate;
        @Schema(description = "이벤트 응모 인스타그램 아이디", type = "string", format = "instagramId", example = "rastle_fashion")
        private String instagramId;
        @Schema(description = "이벤트 응모 전화번호", type = "string", format = "phoneNumber", example = "01012345678")
        private String eventPhoneNumber;
    }
}
