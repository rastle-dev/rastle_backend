package rastle.dev.rastle_backend.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import rastle.dev.rastle_backend.domain.token.exception.ExpireAccessTokenException;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;
import rastle.dev.rastle_backend.global.jwt.JwtTokenProvider;

import java.io.IOException;

import static rastle.dev.rastle_backend.global.common.constants.JwtConstants.AUTHORIZATION_HEADER;
import static rastle.dev.rastle_backend.global.common.constants.JwtConstants.BEARER_PREFIX;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper mapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String jwt = resolveToken(request);
            if (jwt != null) {
                if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                    User user = (User) authentication.getPrincipal();
                    if (user.getUsername() != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } else {
                    throw new ExpireAccessTokenException();
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpireAccessTokenException e) {
            handleErrorResponse(request, response, HttpServletResponse.SC_UNAUTHORIZED, 401L, e);
        } catch (Exception e) {
            handleErrorResponse(request, response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,  500L, e);
        }
    }

    private void handleErrorResponse(HttpServletRequest request, HttpServletResponse response, int statusCode,  long errorCode, Exception e)
        throws IOException {
        log.warn("{} {} {} {}", request.getMethod(), request.getRequestURI(), e.getClass().getName(), e.getMessage());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(statusCode);
        if (request.getHeader("Origin") == null) {
            response.setHeader("Access-Control-Allow-Origin", "https://www.recordyslow.com");
        } else {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }

        try (var writer = response.getWriter()) {
            writer.print(mapper.writeValueAsString(new ErrorResponse(errorCode, e.getMessage())));
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            //            log.info("Extracted Access Token: " + actualToken);
            return bearerToken.substring(7);
        }
        return null;
    }
}
