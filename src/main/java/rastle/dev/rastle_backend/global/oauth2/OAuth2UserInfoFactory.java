package rastle.dev.rastle_backend.global.oauth2;

import rastle.dev.rastle_backend.global.oauth2.provider.KakaoOAuth2UserInfo;
import rastle.dev.rastle_backend.global.oauth2.provider.NaverOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String loginType, Map<String, Object> attributes) {
        return switch (loginType) {
            case "KAKAO" -> new KakaoOAuth2UserInfo(attributes);
            case "NAVER" -> new NaverOAuth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("Invalid Provider Type.");
        };
    }
}
