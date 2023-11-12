package rastle.dev.rastle_backend.global.oauth2.provider;

import rastle.dev.rastle_backend.domain.Member.model.UserLoginType;
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
        return safelyGetNestedString("kakao_account", "profile", "nickname");
    }

    @Override
    public String getEmail() {
        return safelyGetNestedString("kakao_account", "email");
    }

//    @Override
//    public String getPhoneNumber() {
//        return safelyGetNestedString("kakao_account", "phone_number");
//    }

    @Override
    public String getProvider() {
        return UserLoginType.KAKAO.toString();
    }

    private String safelyGetNestedString(String... keys) {
        Object current = attributes;

        for (String key : keys) {
            if (current instanceof Map map) {
                current = map.get(key);
            } else {
                return null;
            }
        }

        if (current instanceof String strValue) {
            return strValue;
        }

        return null;
    }
}
