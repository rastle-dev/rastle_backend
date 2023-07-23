package rastle.dev.rastle_backend.domain.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Member.model.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
}
