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

        Object name = properties.get("name");
        if (name == null) {
            return "";
        }
        return (String) name;
    }

    @Override
    public String getEmail() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");
        if (properties == null) {
            return null;
        }
        Object email = properties.get("email");
        if (email == null) {
            return "";
        }
        return (String) email;
    }

    @Override
    public String getPhoneNumber() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("kakao_account");
        if (properties == null) {
            return null;
        }
        Object phoneNumber = properties.get("phone_number");
        if (phoneNumber == null) {
            return "";
        }
        return (String) phoneNumber;
    }

    @Override
    public String getProvider() {
        return UserLoginType.KAKAO.toString();
    }
}
