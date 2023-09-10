package rastle.dev.rastle_backend.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.domain.Token.dto.TokenDTO.TokenInfoDTO;
import rastle.dev.rastle_backend.global.security.CustomUserDetailsService;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static rastle.dev.rastle_backend.global.common.constants.JwtConstants.*;

@Component
@Slf4j
public class JwtTokenProvider {
    private final Key key;
    private final RedisTemplate<String, String> redisTemplate;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret, RedisTemplate<String, String> redisTemplate,
            CustomUserDetailsService customUserDetailsService) {

        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.redisTemplate = redisTemplate;
        this.customUserDetailsService = customUserDetailsService;
    }

    // 토큰 생성 메서드
    public TokenInfoDTO generateTokenDto(Authentication authentication) {
        long now = (new Date()).getTime();
        String authorities = getAuthorities(authentication);
        String accessToken = buildToken(authentication.getName(), authorities, now + ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = buildToken(authentication.getName(), null, now + REFRESH_TOKEN_EXPIRE_TIME);

        return TokenInfoDTO.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(now + ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken(refreshToken).build();
    }

    // 로그인 시 토큰 생성 메서드
    public TokenInfoDTO generateTokenDto(Authentication authentication, HttpServletResponse response) {
        long now = (new Date()).getTime();
        String authorities = getAuthorities(authentication);
        log.info(authentication.getName()); // 유저 id
        String accessToken = buildToken(authentication.getName(), authorities, now + ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = buildToken(authentication.getName(), null, now + REFRESH_TOKEN_EXPIRE_TIME);

        storeRefreshTokenInRedis(authentication.getName(), refreshToken);
        storeRefreshTokenInCookie(response, refreshToken);

        return TokenInfoDTO.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpiresIn(now + ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken(refreshToken).build();
    }

    // 인증 객체로부터 권한 정보 추출
    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    // JWT 토큰 생성 로직
    private String buildToken(String subject, String authorities, long expiration) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(expiration))
                .signWith(key, SignatureAlgorithm.HS512);
        if (authorities != null) {
            builder.claim(AUTHORITIES_KEY, authorities);
        }
        return builder.compact();
    }

    // 액세스 토큰으로부터 인증 객체 생성
    public Authentication getAuthentication(String accessToken) {
        if (validateToken(accessToken)) {
            Claims claims = parseClaims(accessToken);
            validateClaims(claims);
            Collection<? extends GrantedAuthority> authorities = extractAuthorities(claims);
            UserDetails principal = new User(claims.getSubject(), "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, "", authorities);
        } else {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.info("JWT 토큰 검증 실패: " + e.getMessage());
            return false;
        }
    }

    // 토큰 파싱
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 클레임 유효성 검사
    private void validateClaims(Claims claims) {
        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");
        }
    }

    // 권한 정보 추출
    private Collection<? extends GrantedAuthority> extractAuthorities(Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // 리프레시 토큰을 Redis에 저장
    private void storeRefreshTokenInRedis(String username, String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(username, refreshToken);
        redisTemplate.expire(username, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    // 리프레시 토큰을 http only 쿠키에 저장 및 secure 설정
    private void storeRefreshTokenInCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/auth")
                .sameSite("None")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    // 리프레시 토큰 반환
    public String getRefreshTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        return Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 없습니다."));
    }

    // 리프레시 토큰으로부터 인증 객체 생성
    public Authentication getAuthenticationFromRefreshToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            Claims claims = parseClaims(refreshToken);
            String userId = claims.getSubject();
            UserDetails userDetails = customUserDetailsService.loadUserById(userId);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } else {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }
    }

    // 액세스 토큰 발급
    public String generateAccessToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);
        long now = (new Date()).getTime();
        String accessToken = buildToken(authentication.getName(), authorities, now + ACCESS_TOKEN_EXPIRE_TIME);

        storeRefreshTokenInRedis(authentication.getName(), accessToken);
        return accessToken;
    }

}
