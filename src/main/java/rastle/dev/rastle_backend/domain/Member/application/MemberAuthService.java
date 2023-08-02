package rastle.dev.rastle_backend.domain.Member.application;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.SignUpDto;
import rastle.dev.rastle_backend.domain.Member.model.Member;
import rastle.dev.rastle_backend.domain.Member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberAuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignUpDto signUp(SignUpDto signUpDto) {
        // 회원가입 요청 DTO의 이메일이 이미 존재하는지 확인
        // 이걸 따로 빼는게 좋을까?
        Optional<Member> existingMemberOptional = memberRepository.findByEmail(signUpDto.getEmail());
        if (existingMemberOptional.isPresent()) {
            throw new IllegalArgumentException("Email is already registered.");
        }

        signUpDto.encode(passwordEncoder);
        Member entity = signUpDto.toEntity();
        memberRepository.save(entity);

        return signUpDto;
    }
}
