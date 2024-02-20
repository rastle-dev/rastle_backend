package rastle.dev.rastle_backend.domain.member.application;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rastle.dev.rastle_backend.domain.cart.model.Cart;
import rastle.dev.rastle_backend.domain.cart.repository.mysql.CartRepository;
import rastle.dev.rastle_backend.domain.coupon.model.Coupon;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.domain.member.dto.MemberAuthDTO.LoginDto;
import rastle.dev.rastle_backend.domain.member.dto.MemberAuthDTO.SignUpDto;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.model.RecipientInfo;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.domain.token.dto.TokenDTO.TokenInfoDTO;
import rastle.dev.rastle_backend.global.error.exception.InvalidRequestException;
import rastle.dev.rastle_backend.global.jwt.JwtTokenProvider;

import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberAuthService {
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final CouponRepository couponRepository;

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
        RecipientInfo recipientInfo = new RecipientInfo();
        recipientInfo.setRecipientName(signUpDto.getUsername());
        recipientInfo.setRecipientPhoneNumber(signUpDto.getPhoneNumber());
        entity.updateRecipientInfo(recipientInfo);
        memberRepository.save(entity);
        Cart build = Cart.builder().member(entity).build();
        cartRepository.save(build);
        Coupon coupon = Coupon.builder().discount(3000).name("회원가입 축하 쿠폰").member(entity).build();
        couponRepository.save(coupon);
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
    public ResponseEntity<String> login(LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = authenticate(loginDto);
        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authentication, response);
        HttpHeaders responseHeaders = createAuthorizationHeader(request, tokenInfoDTO.getAccessToken());
        return new ResponseEntity<>("로그인 성공", responseHeaders, HttpStatus.OK);
    }

    private Authentication authenticate(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken token = loginDto.toAuthentication();
        return authenticationManagerBuilder.getObject().authenticate(token);
    }

    private HttpHeaders createAuthorizationHeader(HttpServletRequest request, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        if (request.getHeader("Origin") == null) {
            headers.set("Access-Control-Allow-Origin", "https://recordyslow.com");
        } else {
            headers.set("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }
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
        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(true)
                // .domain("recordyslow.com")
                // .sameSite("Strict")
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
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
        String username = authentication.getName();
        String storedToken = redisTemplate.opsForValue().get(username);
        log.info("Username: " + username);
        log.info("Refresh Token: " + refreshToken);
        log.info("Stored Token: " + storedToken);

        if (storedToken != null && storedToken.equals(refreshToken)) {
            String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);

            HttpHeaders responseHeaders = createAuthorizationHeader(request, newAccessToken);
            return new ResponseEntity<>("액세스 토큰 재발급 성공", responseHeaders, HttpStatus.OK);
        } else if (storedToken == null) {
            return new ResponseEntity<>("액세스 토큰 재발급 실패: 저장된 토큰이 없습니다.", HttpStatus.UNAUTHORIZED);
        } else {
            return new ResponseEntity<>("액세스 토큰 재발급 실패: 저장된 토큰과 입력한 토큰이 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    public static boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        return authorities.stream().filter(o -> o.getAuthority().equals("ROLE_ADMIN")).findAny().isPresent();
    }
}
