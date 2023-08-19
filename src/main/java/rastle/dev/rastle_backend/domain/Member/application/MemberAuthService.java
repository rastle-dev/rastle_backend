package rastle.dev.rastle_backend.domain.Member.application;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.LoginDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.SignUpDto;
import rastle.dev.rastle_backend.domain.Member.model.Member;
import rastle.dev.rastle_backend.domain.Member.repository.MemberRepository;
import rastle.dev.rastle_backend.domain.Token.dto.TokenDTO.TokenInfoDTO;
import rastle.dev.rastle_backend.global.jwt.JwtTokenProvider;

import static rastle.dev.rastle_backend.global.common.constants.JwtConstants.REFRESH_TOKEN_EXPIRE_TIME;

@Service
@RequiredArgsConstructor
public class MemberAuthService {
    private final MemberRepository memberRepository;
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
        signUpDto.encode(passwordEncoder);
        Member entity = signUpDto.toEntity();
        memberRepository.save(entity);

        return signUpDto;
    }

    /**
     * 이메일 중복 확인
     * 
     * @param email 이메일
     * @return 이메일 중복 여부
     */
    @Transactional
    public boolean isEmailExists(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    /**
     * 로그인
     * 
     * @param loginDto
     * @return accessToken
     */
    @Transactional
    public String login(LoginDto loginDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = loginDto.toAuthentication();

        Authentication authenticate = authenticationManagerBuilder.getObject()
                .authenticate(usernamePasswordAuthenticationToken);

        TokenInfoDTO tokenInfoDTO = jwtTokenProvider.generateTokenDto(authenticate);

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(authenticate.getName(), tokenInfoDTO.getRefreshToken());
        redisTemplate.expire(authenticate.getName(), REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);

        return tokenInfoDTO.getAccessToken();
    }
}
