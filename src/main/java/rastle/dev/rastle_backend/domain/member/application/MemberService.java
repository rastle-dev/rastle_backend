package rastle.dev.rastle_backend.domain.member.application;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.LoginMemberInfoDto;
import rastle.dev.rastle_backend.domain.member.model.RecipientInfo;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 로그인한 멤버 정보 조회
     * 
     * @param memberId 로그인한 멤버 아이디
     * @return 로그인한 멤버 정보
     */
    @Transactional
    public LoginMemberInfoDto getLoginMemberInfo(Long memberId) {
        return memberRepository.findMemberInfoById(memberId).orElseThrow(NotFoundByIdException::new);
    }

    /**
     * 비밀번호 변경
     * 
     * @param memberId    멤버 아이디
     * @param newPassword 새로운 비밀번호
     * @return void
     */
    @Transactional
    public void changePassword(Long memberId, String newPassword) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.updatePassword(encodedPassword);
    }

    /**
     * 회원 탈퇴
     * 
     * @param memberId
     */
    @Transactional
    public void deleteMember(HttpServletResponse response, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        redisTemplate.delete(username);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", null)
                .maxAge(0)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .domain("recordyslow.com")
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        memberRepository.delete(member);
    }

    /**
     * 배송지 정보 갱신
     * 
     * @param memberId
     * @param newAddress
     */
    @Transactional
    public void updateMemberRecipientInfo(Long memberId, RecipientInfo recipientInfo) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        member.updateRecipientInfo(recipientInfo);
    }

    /**
     * 배송지 정보 조회
     * 
     * @param memberId
     */

    @Transactional
    public RecipientInfo getMemberRecipientInfo(Long memberId) {
        return memberRepository.findRecipientInfoById(memberId).orElseThrow(NotFoundByIdException::new);
    }

    /**
     * 전화번호 갱신
     * 
     * @param memberId
     * @param newPhoneNumber
     */

    @Transactional
    public void updateMemberPhoneNumber(Long memberId, String newPhoneNumber) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        member.updatePhoneNumber(newPhoneNumber);
    }
}
