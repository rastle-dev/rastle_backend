package rastle.dev.rastle_backend.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.domain.member.exception.LogoutMemberException;
import rastle.dev.rastle_backend.global.jwt.JwtTokenProvider;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
    private final RedisTemplate<String, String> redisTemplate;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
        throws IOException, ServletException {
        log.info("로그아웃 호출됐음");
        setDefaultTargetUrl("https://api.recordyslow.com/auth/logout-redirect");
        String token = request.getHeader("Authorization").split(" ")[1];
        log.info("token = " + token);
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        User principal = (User) auth.getPrincipal();

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        if (valueOperations.get(principal.getUsername()) == null) {
            throw new LogoutMemberException();
        }
        valueOperations.getAndDelete(principal.getUsername());

        log.info(request.getRequestURI());
        super.onLogoutSuccess(request, response, authentication);
    }

}