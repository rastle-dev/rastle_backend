package rastle.dev.rastle_backend.global.oauth2.provider;

import rastle.dev.rastle_backend.domain.member.model.UserLoginType;
import rastle.dev.rastle_backend.global.oauth2.OAuth2UserInfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo {

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        Object id = attributes.get("id");
        return id != null ? id.toString() : null;
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");
        if (properties == null) {
            return null;
        }

        return (String) properties.get("name");
    }

    @Override
    public String getEmail() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");
        if (properties == null) {
            return null;
        }

        return (String) properties.get("email");
    }

    @Override
    public String getPhoneNumber() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");
        if (properties == null) {
            return null;
        }
        return (String) properties.get("phone_number");
    }

    @Override
    public String getProvider() {
        return UserLoginType.KAKAO.toString();
    }
}
