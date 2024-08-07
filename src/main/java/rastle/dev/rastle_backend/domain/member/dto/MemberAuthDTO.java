package rastle.dev.rastle_backend.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import rastle.dev.rastle_backend.domain.member.model.Authority;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.model.UserLoginType;

public class MemberAuthDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "회원가입 요청 DTO")
    public static class SignUpDto {
        @Schema(description = "이메일", type = "string", format = "email", example = "example@email.com", required = true)
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email
        private String email;

        @Schema(description = "비밀번호", type = "string", format = "password", example = "password!", required = true)
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
        @Pattern(regexp = ".*[^a-zA-Z0-9].*", message = "비밀번호에는 최소 하나의 특수 문자를 포함해야 합니다.")
        private String password;

        @Schema(description = "이름", type = "string", format = "name", example = "홍길동", required = true)
        @NotBlank(message = "이름을 입력해주세요.")
        private String username;

        @Schema(description = "전화번호", type = "string", format = "phone", example = "01012345678", required = true)
        @NotBlank(message = "전화번호를 입력해주세요.")
        @Pattern(regexp = "^010\\d{8}$", message = "유효한 전화번호가 아닙니다.")
        private String phoneNumber;

        public void encode(PasswordEncoder passwordEncoder) {
            this.password = passwordEncoder.encode(this.password);
        }

        public Member toEntity() {
            return Member.builder()
                .email(email)
                .password(password)
                .userLoginType(UserLoginType.EMAIL)
                .authority(Authority.ROLE_USER)
                .phoneNumber(phoneNumber)
                .userName(username)
                .deleted(false)
                .build();
        }
    }

    // @Builder
    // @AllArgsConstructor
    // @NoArgsConstructor
    // @Getter
    // @Schema(description = "관리자 회원가입 요청 DTO")
    // public static class AdminSignUpDto {
    // @Schema(description = "아이디")
    // private String email;

    // @Schema(description = "비밀번호")
    // private String password;

    // @Schema(description = "이름")
    // @NotBlank(message = "이름을 입력해주세요.")
    // private String username;

    // public void encode(PasswordEncoder passwordEncoder) {
    // this.password = passwordEncoder.encode(this.password);
    // }

    // public Member toEntity() {
    // return Member.builder()
    // .email(email)
    // .password(password)
    // .authority(Authority.ROLE_ADMIN)
    // .userName(username)
    // .build();
    // }
    // }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "로그인 요청 DTO")
    public static class LoginDto {
        @Schema(description = "이메일", type = "string", format = "email", example = "example@email.com", required = true)
        private String email;
        @Schema(description = "비밀번호", type = "string", format = "password", example = "password!", required = true)
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "이메일 인증 요청 DTO")
    public static class EmailCertificationDto {
        @Schema(description = "인증할 이메일", type = "string", format = "email", example = "example@email.com", required = true)
        private String email;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "이메일 인증 번호 확인 요청 DTO")
    public static class EmailCertificationCheckDto {
        @Schema(description = "인증할 이메일", type = "string", format = "email", example = "example@email.com", required = true)
        private String email;
        @Schema(description = "인증 번호", type = "string", format = "code", example = "123456", required = true)
        private String code;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "비밀번호 초기화 요청 DTO")
    public static class PasswordResetRequestDto {
        @Schema(description = "비밀번호 초기화 요청 이메일", type = "string", format = "email", example = "example@email.com", required = true)
        private String email;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "인증된 유저 정보 요청 DTO")
    public static class UserPrincipalInfoDto {
        private Long id;
        private String password;
        private UserLoginType userLoginType;
        private Authority authority;
    }
}
