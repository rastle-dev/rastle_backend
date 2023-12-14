package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.common.enums.DeliveryStatus;
import rastle.dev.rastle_backend.global.common.enums.PaymentStatus;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.InheritanceType.JOINED;

/**
 * 주문이 완료된 데이터를 관리하는 객체
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_detail")
@Inheritance(strategy = JOINED)
public class OrderDetail extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;
    private String tel;
    private String email;
    private String postcode;
    @Column(name = "delivery_address")
    private String deliveryAddress;
    @Column(name = "order_number", unique = true)
    private String orderNumber;
    @NotNull
    @Enumerated(STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;
    @NotNull
    @Enumerated(STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "orderDetail", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<OrderProduct> orderProduct = new ArrayList<>();

    @Builder
    public OrderDetail(String userName, String tel, String email, String postcode, String deliveryAddress, String orderNumber, DeliveryStatus deliveryStatus, PaymentStatus paymentStatus, Member member) {

        this.userName = userName;
        this.tel = tel;
        this.email = email;
        this.postcode = postcode;
        this.deliveryAddress = deliveryAddress;
        this.orderNumber = orderNumber;
        this.deliveryStatus = deliveryStatus;
        this.paymentStatus = paymentStatus;
        this.member = member;
    }

    public void updateDeliveryStatus(DeliveryStatus status) {
        this.deliveryStatus = status;
    }

    public void updateDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void updatePostCode(String postcode) {
        this.postcode = postcode;
    }

    public void updateOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
