package rastle.dev.rastle_backend.global.oauth2.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import rastle.dev.rastle_backend.domain.Member.model.UserLoginType;
import rastle.dev.rastle_backend.domain.Member.repository.MemberRepository;
import rastle.dev.rastle_backend.global.jwt.JwtTokenProvider;
import rastle.dev.rastle_backend.global.oauth2.OAuth2UserInfo;
import rastle.dev.rastle_backend.global.oauth2.OAuth2UserInfoFactory;
import rastle.dev.rastle_backend.global.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository;
import rastle.dev.rastle_backend.global.util.CookieUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static rastle.dev.rastle_backend.global.oauth2.repository.OAuth2AuthorizationRequestBasedOnCookieRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("authentication success");

        log.info("authentication : " + authentication.getName());
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("attributes : " + attributes);
        UserLoginType provider = memberRepository.findById(Long.parseLong(authentication.getName())).get()
                .getUserLoginType();

        log.info("proivder", provider);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                provider.getType(), attributes);
        String targetUrl;

        if (Objects.equals(authentication.getName(), "null")) {
            targetUrl = determineTargetUrlForFirstLogin(request, response, authentication, oAuth2UserInfo);
        } else {
            targetUrl = determineTargetUrlForLoginAgain(request, response, authentication);
        }

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        log.info("target url : " + targetUrl);
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrlForFirstLogin(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication, OAuth2UserInfo oAuth2UserInfo) {
        String targetUrl = getTargetUrlFromCookie(request);
        Map<String, Object> queryParams = new HashMap<>();

        jwtTokenProvider.generateTokenDto(authentication, response);

        queryParams.put("created", true);
        queryParams.put("email", oAuth2UserInfo.getEmail());
        queryParams.put("userName", URLEncoder.encode(oAuth2UserInfo.getName(), StandardCharsets.UTF_8));
        queryParams.put("loginType", oAuth2UserInfo.getProvider());
        // queryParams.put("accessToken", tokenDto.getAccessToken());

        return buildUriComponents(targetUrl, queryParams).toUriString();
    }

    protected String determineTargetUrlForLoginAgain(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) {
        String targetUrl = getTargetUrlFromCookie(request);
        Map<String, Object> queryParams = new HashMap<>();

        jwtTokenProvider.generateTokenDto(authentication, response);

        // queryParams.put("accessToken", tokenDto.getAccessToken());
        queryParams.put("created", false);

        return buildUriComponents(targetUrl, queryParams).toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private String getTargetUrlFromCookie(HttpServletRequest request) {
        return CookieUtil.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(getDefaultTargetUrl());
    }

    private UriComponents buildUriComponents(String targetUrl, Map<String, Object> queryParams) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(targetUrl);
        queryParams.forEach((key, value) -> builder.queryParam(key, value));
        return builder.build();
    }

}