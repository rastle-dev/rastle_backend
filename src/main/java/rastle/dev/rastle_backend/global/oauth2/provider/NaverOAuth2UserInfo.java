package rastle.dev.rastle_backend.global.oauth2.provider;

import rastle.dev.rastle_backend.domain.member.model.UserLoginType;
import rastle.dev.rastle_backend.global.oauth2.OAuth2UserInfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return (String) response.get("id");
    }

    @Override
    public String getName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        Object name = response.get("name");
        if (name == null) {
            return "";
        }
        return (String) name;
    }

    @Override
    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }
        Object email = response.get("email");
        if (email == null) {
            return "";
        }
        return (String) email;
    }

    @Override
    public String getPhoneNumber() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        Object mobile = response.get("mobile");
        if (mobile == null) {
            return "";
        }
        return (String) mobile;
    }

    @Override
    public void setPhoneNumber(String phoneNumber) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        if (response != null) {
            response.put("mobile", phoneNumber);
        }
    }

    @Override
    public String getProvider() {
        return UserLoginType.NAVER.toString();
    }
}
