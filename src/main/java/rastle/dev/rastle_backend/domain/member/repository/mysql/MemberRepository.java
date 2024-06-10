package rastle.dev.rastle_backend.domain.member.repository.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.member.dto.MemberAuthDTO.UserPrincipalInfoDto;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.LoginMemberInfoDto;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.model.RecipientInfo;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m.id FROM Member m WHERE m.email = :email AND m.deleted = false ")
    Optional<Long> findUserIdByEmail(@Param("email") String email);

    @Query("SELECT NEW rastle.dev.rastle_backend.domain.member.dto.MemberAuthDTO$UserPrincipalInfoDto(m.id, m.password, m.userLoginType, m.authority) FROM Member m WHERE m.email = :email AND m.deleted=false ")
    Optional<UserPrincipalInfoDto> findUserPrincipalInfoByEmail(@Param("email") String email);

    @Query("SELECT NEW rastle.dev.rastle_backend.domain.member.dto.MemberAuthDTO$UserPrincipalInfoDto(m.id, m.password, m.userLoginType, m.authority) FROM Member m WHERE m.id = :id AND m.deleted=false ")
    Optional<UserPrincipalInfoDto> findUserPrincipalInfoById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Member m SET m.password = :newPassword WHERE m.id = :memberId AND m.deleted=false")
    void updatePassword(@Param("memberId") Long memberId, @Param("newPassword") String newPassword);

    @Modifying
    @Query("UPDATE Member m SET m.deleted=true WHERE m.id = :id")
    void safeDelete(@Param("id") Long id);

    Optional<Member> findByEmailAndDeleted(String email, Boolean deleted);

    @Query("SELECT NEW rastle.dev.rastle_backend.domain.member.dto.MemberDTO$LoginMemberInfoDto(m.email, m.userName, m.phoneNumber, m.userLoginType, m.authority) FROM Member m WHERE m.id = :id AND m.deleted=false")
    Optional<LoginMemberInfoDto> findLoginMemberInfoById(@Param("id") Long id);

    @Query("SELECT m FROM Member m WHERE m.authority = 'ROLE_USER' AND m.deleted=false ")
    Page<Member> findAllUsers(Pageable pageable);

    @Query("SELECT m.recipientInfo FROM Member m WHERE m.id = :id AND m.deleted = false")
    Optional<RecipientInfo> findRecipientInfoById(@Param("id") Long id);
}
