package rastle.dev.rastle_backend.global.security;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.model.UserPrincipal;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> byEmail = memberRepository.findByEmail(username);
        if (byEmail.isPresent()) {
            return UserPrincipal.create(byEmail.get());
        } else {
            throw new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다.");
        }
    }

    public UserDetails loadUserById(String userIdString) {
        Long userId = Long.parseLong(userIdString);
        Optional<Member> byId = memberRepository.findById(userId);
        if (byId.isPresent()) {
            return UserPrincipal.create(byId.get());
        } else {
            throw new UsernameNotFoundException(userId + " -> 데이터베이스에서 찾을 수 없습니다.");
        }
    }

}
