package rastle.dev.rastle_backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import rastle.dev.rastle_backend.global.cache.StringRedisTemplate;
import rastle.dev.rastle_backend.global.filter.IpAuthenticationFilter;
import rastle.dev.rastle_backend.global.jwt.JwtAccessDeniedHandler;
import rastle.dev.rastle_backend.global.jwt.JwtAuthenticationEntryPoint;
import rastle.dev.rastle_backend.global.jwt.JwtTokenProvider;
import rastle.dev.rastle_backend.global.oauth2.application.CustomOAuth2UserService;
import rastle.dev.rastle_backend.global.oauth2.handler.OAuth2AuthenticationFailureHandler;
import rastle.dev.rastle_backend.global.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import rastle.dev.rastle_backend.global.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import rastle.dev.rastle_backend.global.security.CustomLogoutSuccessHandler;

import java.util.Arrays;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final StringRedisTemplate stringRedisTemplate;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
    private final IpAuthenticationFilter ipAuthenticationFilter;
    private final ObjectMapper mapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .httpBasic().disable()
            .csrf().disable();

        // exception handling 할때 우리가 만든 클래스 추가
        http
            .exceptionHandling()
            .accessDeniedHandler(jwtAccessDeniedHandler)
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)

            .and()
            .headers()
            .frameOptions()
            .sameOrigin()

            .and()
            .sessionManagement()
            .sessionCreationPolicy(STATELESS)

            .and()
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.OPTIONS, "**").permitAll()
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers(HttpMethod.GET, "/env_profile").permitAll()
                .requestMatchers(HttpMethod.GET, "/actuator/health").permitAll()
                .requestMatchers("/").authenticated()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/login/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/category/**").permitAll()
                .requestMatchers("/member/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v1/api-docs/**").permitAll()
                .requestMatchers("/product/**").permitAll()
                .requestMatchers("/bundle/**").permitAll()
                .requestMatchers("/event/**").permitAll()
                .requestMatchers("/orders/**").authenticated()
                .requestMatchers("/coupon/**").authenticated()
                .requestMatchers("/payments/portone-webhook").permitAll()
                .requestMatchers("/payments/prepare").authenticated()
                .requestMatchers("/payments/complete").authenticated()
                .requestMatchers("/payments/completeMobile").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/cart/**").hasRole("USER")
                .requestMatchers("/delivery/**").permitAll()
                .requestMatchers("/jmeter/**").permitAll()
                .requestMatchers("/hibernate/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/rest-docs").permitAll());

        http
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorization") // default
            .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository)
            .and()
            .redirectionEndpoint()
            .baseUri("/oauth2/callback/*")
            .and()
            .userInfoEndpoint()
            .userService(customOAuth2UserService)
            .and()
            .successHandler(oAuth2AuthenticationSuccessHandler)
            .failureHandler(oAuth2AuthenticationFailureHandler);

        http
            .apply(new JwtSecurityConfig(jwtTokenProvider, mapper, stringRedisTemplate, ipAuthenticationFilter))
            .and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/auth/logout-redirect")
            .clearAuthentication(true)
            .logoutSuccessHandler(customLogoutSuccessHandler);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
            Arrays.asList("http://localhost:3000", "https://localhost:3000",
                "https://www.rastle.site", "https://rastle.site", "https://api.rastle.site", "https://optimize.d2fyka5dusxft6.amplifyapp.com", "http://localhost:8080"));
        configuration.setAllowedMethods(
            Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT", "OPTIONS", "PATCH"));
        configuration.addAllowedHeader(("*"));
        configuration.addExposedHeader("Authorization");
        // configuration.addExposedHeader("Set-Cookie");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
