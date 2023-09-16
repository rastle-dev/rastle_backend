package rastle.dev.rastle_backend.domain.Member.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.LoginMemberInfoDto;
import rastle.dev.rastle_backend.domain.Member.model.Member;
import rastle.dev.rastle_backend.domain.Member.repository.MemberRepository;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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
    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        memberRepository.delete(member);
    }
}
