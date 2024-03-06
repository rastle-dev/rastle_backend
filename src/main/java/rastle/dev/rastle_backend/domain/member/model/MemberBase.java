package rastle.dev.rastle_backend.domain.member.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member_base")
@Inheritance(strategy = InheritanceType.JOINED)
public class MemberBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_login_type")
    private UserLoginType userLoginType;

    @Enumerated(EnumType.STRING)
    protected Authority authority;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    public MemberBase(String email, String password, UserLoginType userLoginType, Authority authority) {
        this.email = email;
        this.password = password;
        this.userLoginType = userLoginType;
        this.authority = authority;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
