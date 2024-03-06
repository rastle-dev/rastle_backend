package rastle.dev.rastle_backend.domain.member.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.cart.model.Cart;
import rastle.dev.rastle_backend.domain.coupon.model.Coupon;
import rastle.dev.rastle_backend.domain.event.model.EventProductApply;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member extends MemberBase {
    @Column(name = "user_name")
    private String userName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Convert(converter = RecipientInfo.AddressConverter.class)
    @Column(name = "recipient_info", columnDefinition = "JSON")
    private RecipientInfo recipientInfo;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Coupon> coupons = new ArrayList<>();

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Cart cart;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<EventProductApply> eventProductApplies = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<OrderDetail> orders = new ArrayList<>();

    @Builder
    public Member(String email, String password, UserLoginType userLoginType, Authority authority, String userName,
                  String phoneNumber) {
        super(email, password, userLoginType, authority);
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

    public void updatePassword(String newPassword) {
        super.updatePassword(newPassword);
    }

    public void updateRecipientInfo(RecipientInfo newRecipientInfo) {
        this.recipientInfo = newRecipientInfo;
    }

    public void updatePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }
}
