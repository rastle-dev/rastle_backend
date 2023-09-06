package rastle.dev.rastle_backend.domain.Member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.MemberInfoDto;
import rastle.dev.rastle_backend.domain.Member.model.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("SELECT NEW rastle.dev.rastle_backend.domain.Member.dto.MemberDTO$MemberInfoDto(m.email, m.userName, m.phoneNumber, m.userLoginType, m.authority) FROM Member m WHERE m.id = :id")
    Optional<MemberInfoDto> findMemberInfoById(@Param("id") Long id);
}
