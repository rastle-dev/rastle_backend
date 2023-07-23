package rastle.dev.rastle_backend.domain.Member.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Cart.model.Cart;
import rastle.dev.rastle_backend.domain.Event.model.EventProductApply;
import rastle.dev.rastle_backend.domain.Orders.model.Orders;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member", catalog = "rastle_db")
public class Member extends MemberBase {
    @Column(name = "user_name")
    private String userName;
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private Coupon coupon;

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private EventProductApply eventProductApply;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private Orders orders;
}
