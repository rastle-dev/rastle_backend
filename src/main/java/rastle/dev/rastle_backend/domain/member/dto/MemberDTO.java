package rastle.dev.rastle_backend.domain.member.dto;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.member.model.Authority;
import rastle.dev.rastle_backend.domain.member.model.UserLoginType;

public class MemberDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "로그인한 멤버 정보 조회 요청 DTO")
    public static class LoginMemberInfoDto {
        @Schema(description = "이메일", type = "string", format = "email", example = "example@email.com")
        private String email;
        @Schema(description = "이름", type = "string", format = "name", example = "홍길동")
        private String userName;
        @Schema(description = "전화번호", type = "string", format = "phone", example = "01012345678")
        private String phoneNumber;
        @Schema(description = "유저 로그인 타입", type = "string", format = "userLoginType", example = "EMAIL")
        private UserLoginType userLoginType;
        @Schema(description = "권한", type = "string", format = "authority", example = "ROLE_USER")
        private Authority authority;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "비밀번호 변경 요청 DTO")
    public static class PasswordDto {
        @Schema(description = "새로운 비밀번호", type = "string", format = "password", example = "password!")
        @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
        @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "비밀번호에는 최소 하나의 특수 문자를 포함해야 합니다.")
        private String newPassword;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "전체 멤버 정보 조회 요청 DTO")
    public static class MemberInfoDto {
        @Schema(description = "이메일", type = "string", format = "email", example = "example@email.com")
        private String email;
        @Schema(description = "유저 로그인 타입", type = "string", format = "userLoginType", example = "EMAIL")
        private UserLoginType userLoginType;
        @Schema(description = "이름", type = "string", format = "name", example = "홍길동")
        private String userName;
        @Schema(description = "전화번호", type = "string", format = "phone", example = "01012345678", required = true)
        private String phoneNumber;
        @Schema(description = "주소", type = "string", format = "address", example = "서울시 강남구 어딘가로")
        private String address;
        @Schema(description = "생성 일시", type = "string", format = "createdDate", example = "2021-08-01T00:00:00")
        private LocalDateTime createdDate;
        @Schema(description = "주문 정보", type = "list", format = "orderDetails")
        private List<OrderDetail> allOrderDetails;

        @Getter
        @Builder
        public static class OrderDetail {
            private Long orderId;
            private List<OrderProductDetail> orderProducts;
        }

        @Getter
        @Builder
        public static class OrderProductDetail {
            private String color;
            private String size;
            private int count;
            private String productName;
        }
    }
}
