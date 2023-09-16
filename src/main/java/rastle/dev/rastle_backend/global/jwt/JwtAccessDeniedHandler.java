package rastle.dev.rastle_backend.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;

import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        sendResponse(response, accessDeniedException);
    }

    private void sendResponse(HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException {
        String result = objectMapper.writeValueAsString(new ErrorResponse(400L, "접근할 수 있는 권한이 없습니다."));

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
