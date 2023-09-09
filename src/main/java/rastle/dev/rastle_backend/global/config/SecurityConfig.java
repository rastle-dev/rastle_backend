package rastle.dev.rastle_backend.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import rastle.dev.rastle_backend.global.jwt.JwtAccessDeniedHandler;
import rastle.dev.rastle_backend.global.jwt.JwtAuthenticationEntryPoint;
import rastle.dev.rastle_backend.global.jwt.JwtTokenProvider;
import rastle.dev.rastle_backend.global.oauth2.application.CustomOAuth2UserService;
import rastle.dev.rastle_backend.global.oauth2.handler.OAuth2AuthenticationFailureHandler;
import rastle.dev.rastle_backend.global.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import rastle.dev.rastle_backend.global.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import rastle.dev.rastle_backend.global.security.CustomLogoutSuccessHandler;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        private final JwtTokenProvider jwtTokenProvider;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
        private final RedisTemplate<String, String> redisTemplate;
        private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
        private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
        private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
        private final CustomOAuth2UserService customOAuth2UserService;
        private final OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository;
        private final ObjectMapper mapper;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                // CSRF 설정 Disable
                http
                                .httpBasic().disable()
                                .csrf().disable()
                                .cors().configurationSource(corsConfigurationSource());

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
                                                .requestMatchers("/actuator/**").authenticated()
                                                .requestMatchers("/oauth2/**").permitAll()
                                                .requestMatchers("/login/**").permitAll()
                                                .requestMatchers("/auth/**").permitAll()
                                                .requestMatchers("/category/**").permitAll()
                                                .requestMatchers("/member/**").hasRole("USER")
                                                .requestMatchers("/swagger-ui/**").permitAll()
                                                .requestMatchers("/v1/api-docs/**").permitAll()
                                                .requestMatchers("/product/**").permitAll()
                                                .requestMatchers("/market/**").permitAll()
                                                .requestMatchers("/event/**").permitAll()
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
                                .apply(new JwtSecurityConfig(jwtTokenProvider, mapper, redisTemplate))
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

                configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000"));
                configuration.setAllowedMethods(
                                Arrays.asList("HEAD", "POST", "GET", "DELETE", "PUT", "OPTIONS", "PATCH", "PUT"));
                configuration.setAllowedHeaders(List.of("*"));
                configuration.setAllowCredentials(true);
                configuration.setExposedHeaders(List.of("*"));

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}
