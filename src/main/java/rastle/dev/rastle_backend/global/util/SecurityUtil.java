package rastle.dev.rastle_backend.global.util;

import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import rastle.dev.rastle_backend.global.error.exception.NotAuthorizedException;

@NoArgsConstructor
public class SecurityUtil {
    // SecurityContext에 유저 정보가 저장되는 시점
    // Request가 들어올때 JwtFilter의 doFilter에서 저장
    public static Long getCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser")) {
            throw new NotAuthorizedException();
        }

        return Long.parseLong(authentication.getName());
    }

    public static Long getNullableCurrentMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null || authentication.getName().equals("anonymousUser")) {
            return null;
        }
        return Long.parseLong(authentication.getName());
    }
}
