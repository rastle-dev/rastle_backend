package rastle.dev.rastle_backend.domain.member.repository.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.member.dto.MemberAuthDTO.UserPrincipalInfoDto;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.LoginMemberInfoDto;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.MemberInfoDto;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.model.RecipientInfo;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m.id FROM Member m WHERE m.email = :email")
    Optional<Long> findUserIdByEmail(@Param("email") String email);

    @Query("SELECT NEW rastle.dev.rastle_backend.domain.member.dto.MemberAuthDTO$UserPrincipalInfoDto(m.id, m.password, m.userLoginType, m.authority) FROM Member m WHERE m.email = :email")
    Optional<UserPrincipalInfoDto> findUserPrincipalInfoByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE Member m SET m.password = :newPassword WHERE m.id = :memberId")
    void updatePassword(@Param("memberId") Long memberId, @Param("newPassword") String newPassword);

    @Query("SELECT NEW rastle.dev.rastle_backend.domain.member.dto.MemberDTO$MemberInfoDto(" +
        "m.email, m.userLoginType, m.userName, m.phoneNumber, m.recipientInfo, m.createdDate) " +
        "FROM Member m WHERE m.id = :id")
    MemberInfoDto findMemberInfoById(@Param("id") Long id);

    Optional<Member> findByEmail(String email);

    @Query("SELECT NEW rastle.dev.rastle_backend.domain.member.dto.MemberDTO$LoginMemberInfoDto(m.email, m.userName, m.phoneNumber, m.userLoginType, m.authority) FROM Member m WHERE m.id = :id")
    Optional<LoginMemberInfoDto> findLoginMemberInfoById(@Param("id") Long id);

    @Query("SELECT m FROM Member m WHERE m.authority = 'ROLE_USER'")
    Page<Member> findAllUsers(Pageable pageable);

    @Query("SELECT m.recipientInfo FROM Member m WHERE m.id = :id")
    Optional<RecipientInfo> findRecipientInfoById(@Param("id") Long id);
}
