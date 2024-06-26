package rastle.dev.rastle_backend.domain.token.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.security.core.Authentication;

public class TokenDTO {
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TokenInfoDTO {
        private String grantType;

        private String accessToken;

        private Long accessTokenExpiresIn;

        private String refreshToken;

        public TokenIssueDTO toTokenIssueDTO() {
            return TokenIssueDTO.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn)
                .grantType(grantType).build();
        }


    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class TokenIssueDTO {
        @Schema(description = "엑세스 토큰 정보", defaultValue = "엑세스 토큰")
        private String accessToken;
        @Schema(description = "토큰 타입", defaultValue = "토큰 타입 - Bearer")
        private String grantType;

        private Long accessTokenExpiresIn;

    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class ReissueRequest {
        private String token;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TokenClaim {
        Authentication authentication;
        String agent;
        String ip;
    }


}
