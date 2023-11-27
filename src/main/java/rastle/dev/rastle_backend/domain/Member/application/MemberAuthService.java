package rastle.dev.rastle_backend.domain.Member.application;

import java.util.Collection;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.Cart.model.Cart;
import rastle.dev.rastle_backend.domain.Cart.repository.CartRepository;
// import rastle.dev.rastle_backend.domain.Member.dto.MemberAuthDTO.AdminSignUpDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberAuthDTO.LoginDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberAuthDTO.SignUpDto;
import rastle.dev.rastle_backend.domain.Member.model.Member;
import rastle.dev.rastle_backend.domain.Member.repository.MemberRepository;
import rastle.dev.rastle_backend.domain.Token.dto.TokenDTO.TokenInfoDTO;
import rastle.dev.rastle_backend.global.error.exception.InvalidRequestException;
import rastle.dev.rastle_backend.global.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class MemberAuthService {
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /**
     * 회원가입
     * 
     * @param signUpDto 회원가입 요청 DTO
     * @return 회원가입 요청 DTO
     */
    @Transactional
    public SignUpDto signUp(SignUpDto signUpDto) {
        if (isEmailDuplicated(signUpDto.getEmail())) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }
        signUpDto.encode(passwordEncoder);
        Member entity = signUpDto.toEntity();
        memberRepository.save(entity);
        Cart build = Cart.builder().member(entity).build();
        cartRepository.save(build);

        return signUpDto;
    }

    // /**
    // * 관리자 회원가입
    // *
    // * @param signUpDto 관리자 회원가입 요청 DTO
    // * @return 관리자 회원가입 요청 DTO
    // */
    // @Transactional
    // public AdminSignUpDto adminSignUp(AdminSignUpDto adminSignUpDto) {
    // adminSignUpDto.encode(passwordEncoder);
    // Member entity = adminSignUpDto.toEntity();
    // memberRepository.save(entity);

    // return adminSignUpDto;
    // }

    /**
     * 이메일 중복 확인
     * 
     * @param email 이메일
     * @return 이메일 중복 여부
     */
    @Transactional
    public boolean isEmailDuplicated(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    /**
     * 로그인
     * 
     * @param loginDto
     * @return 로그인 성공 여부
     */
    @Transactional
    public ResponseEntity<String> login(LoginDto loginDto, HttpServletResponse response) {
        Authentication authentication = authenticate(loginDto);
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authentication, response);
        HttpHeaders responseHeaders = createAuthorizationHeader(tokenInfoDTO.getAccessToken());
        return new ResponseEntity<>("로그인 성공", responseHeaders, HttpStatus.OK);
    }

    private Authentication authenticate(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken token = loginDto.toAuthentication();
        return authenticationManagerBuilder.getObject().authenticate(token);
    }

    private HttpHeaders createAuthorizationHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    /**
     * 로그아웃
     * 
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @return 로그아웃 성공 여부
     */
    @Transactional
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String username = getCurrentUsername();
        deleteRefreshTokenFromRedis(username);
        deleteRefreshTokenCookie(response);
        return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void deleteRefreshTokenFromRedis(String username) {
        redisTemplate.delete(username);
    }

    private void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        try {
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 액세스 토큰 재발급
     * 
     * @param request
     * @return 액세스 토큰 재발급 성공 여부
     */
    public ResponseEntity<String> refreshAccessToken(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.getRefreshTokenFromRequest(request);
        Authentication authentication = jwtTokenProvider.getAuthenticationFromRefreshToken(refreshToken);
        String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

        HttpHeaders responseHeaders = createAuthorizationHeader(newAccessToken);
        return new ResponseEntity<>("액세스 토큰 재발급 성공", responseHeaders, HttpStatus.OK);
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
    }
}
