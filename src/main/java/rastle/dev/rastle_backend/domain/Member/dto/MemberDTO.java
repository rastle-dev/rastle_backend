package rastle.dev.rastle_backend.domain.Member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Member.model.Authority;
import rastle.dev.rastle_backend.domain.Member.model.UserLoginType;

public class MemberDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "멤버 정보 조회 요청 DTO")
    public static class MemberInfoDto {
        @Schema(description = "이메일")
        private String email;
        @Schema(description = "이름")
        private String userName;
        @Schema(description = "전화번호")
        private String phoneNumber;
        @Schema(description = "유저 로그인 타입")
        private UserLoginType userLoginType;
        @Schema(description = "권한")
        private Authority authority;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "비밀번호 변경 요청 DTO")
    public static class PasswordDto {
        @Schema(description = "새로운 비밀번호")
        private String newPassword;
    }
}
