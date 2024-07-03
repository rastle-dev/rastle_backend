package rastle.dev.rastle_backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rastle.dev.rastle_backend.global.cache.StringRedisTemplate;
import rastle.dev.rastle_backend.global.filter.IpAuthenticationFilter;
import rastle.dev.rastle_backend.global.filter.JwtFilter;
import rastle.dev.rastle_backend.global.jwt.JwtTokenProvider;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;

    private final ObjectMapper mapper;

    private final StringRedisTemplate stringRedisTemplate;

    private final IpAuthenticationFilter ipAuthenticationFilter;

    // tokenProvider를 주입 받아서 JwtFilter를 통해 security 로직에 필터를 등록
    @Override
    public void configure(HttpSecurity httpSecurity) {
        JwtFilter jwtFilter = new JwtFilter(jwtTokenProvider, stringRedisTemplate, mapper);
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(ipAuthenticationFilter, JwtFilter.class);

    }
}
