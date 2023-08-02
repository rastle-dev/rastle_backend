package rastle.dev.rastle_backend.domain.Member.dto;

import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.Member.model.Authority;
import rastle.dev.rastle_backend.domain.Member.model.Member;
import rastle.dev.rastle_backend.domain.Member.model.UserLoginType;

public class MemberDTO {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "회원가입 요청 DTO")
    public static class SignUpDto {
        @Schema(description = "이메일")
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email
        private String email;

        @Schema(description = "비밀번호")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        @Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하로 입력해주세요.")
        private String password;

        @Schema(description = "이름")
        @NotBlank(message = "이름을 입력해주세요.")
        private String username;

        @Schema(description = "전화번호")
        @NotBlank(message = "전화번호를 입력해주세요.")
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
                    .build();
        }
    }
}
