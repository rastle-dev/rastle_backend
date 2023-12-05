package rastle.dev.rastle_backend.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        log.error(String.valueOf(authException.getClass()));
        log.error(authException.getMessage());
        sendResponse(request, response, authException);
    }

    private void sendResponse(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        String result = "서버 에러가 발생했습니다";
        if (authException instanceof BadCredentialsException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(409L, "잘못된 이메일, 비밀번호 입니다."));
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        } else if (authException instanceof InternalAuthenticationServiceException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(404L,
                    "존재하지 않는 멤버입니다."));
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else if (authException instanceof InsufficientAuthenticationException) {
            result = objectMapper.writeValueAsString(new ErrorResponse(403L, "인증 정보가 확인되지 않은 요청입니다."));
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        // response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");

        response.setHeader("Access-Control-Allow-Origin", "https://www.recordyslow.com");
        response.getWriter().write(result);
    }
}