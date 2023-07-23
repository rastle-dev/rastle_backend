package rastle.dev.rastle_backend.domain.Member.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_base", catalog = "rastle_db")
@Inheritance(strategy = InheritanceType.JOINED)
public class MemberBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    protected Authority authority;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_login_type")
    private UserLoginType userLoginType;


}
