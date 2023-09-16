package rastle.dev.rastle_backend.global.oauth2.provider;

import java.util.Map;

import rastle.dev.rastle_backend.domain.Member.model.UserLoginType;
import rastle.dev.rastle_backend.global.oauth2.OAuth2UserInfo;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return safelyGetString("id");
    }

    @Override
    public String getName() {
        return safelyGetString("nickname");
    }

    @Override
    public String getEmail() {
        return safelyGetString("email");
    }

    @Override
    public String getProvider() {
        return UserLoginType.NAVER.toString();
    }

    private String safelyGetString(String key) {
        Object responseObj = attributes.get("response");

        if (responseObj instanceof Map response) {
            Object value = response.get(key);
            if (value instanceof String strValue) {
                return strValue;
            }
        }

        return null;
    }
}
