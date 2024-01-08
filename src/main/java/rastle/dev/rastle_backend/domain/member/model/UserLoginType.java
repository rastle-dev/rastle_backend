package rastle.dev.rastle_backend.domain.member.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserLoginType {
    EMAIL(1, "EMAIL"), KAKAO(2, "KAKAO"), NAVER(3, "NAVER");

    private long id;
    private String type;

    UserLoginType(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public static UserLoginType valueOfLabel(String label) {
        return Arrays.stream(values())
                .filter(value -> value.type.equals(label))
                .findAny()
                .orElse(null);
    }

}