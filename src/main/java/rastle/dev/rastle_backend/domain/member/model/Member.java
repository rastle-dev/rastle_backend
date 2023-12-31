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

    // @Column(name = "zip_code")
    // private String zipCode;

    // @Column(name = "road_address")
    // private String roadAddress;

    // @Column(name = "detail_address")
    // private String detailAddress;

    @Convert(converter = Address.AddressConverter.class)
    @Column(name = "address", columnDefinition = "JSON")
    private Address address;

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

    public void updateAddress(Address newAddress) {
        this.address = newAddress;
    }

    // public void updateZipcode(String newZipcode) {
    // this.zipCode = newZipcode;
    // }

    // public void updateRoadAddress(String newRoadAddress) {
    // this.roadAddress = newRoadAddress;
    // }

    // public void updateDetailAddress(String newDetailAddress) {
    // this.detailAddress = newDetailAddress;
    // }
}
