package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;

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
@Table(name = "order_detail", indexes = {
    @Index(name = "idx_order_number", columnList = "order_number", unique = true)
})
@Inheritance(strategy = JOINED)
public class OrderDetail extends BaseTimeEntity {
    /*
    TODO 주문 상태 값 추가 필요할듯
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;
    @Column(name = "order_name")
    private String orderName;
    private String tel;
    private String email;
    private String postcode;
    @Column(name = "delivery_address")
    private String deliveryAddress;
    @Column(name = "order_number", unique = true)
    private String orderNumber;
    @Column(name = "payment_price")
    private Long paymentPrice;
    @Column(name = "delivery_price")
    private Long deliveryPrice;
    @Enumerated(STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Column(name = "imp_id")
    private String impId;
    @Column(name = "delivery_msg")
    private String deliveryMsg;
    @Column(name = "tracking_number")
    private String trackingNumber;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "orderDetail", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<OrderProduct> orderProduct = new ArrayList<>();

    @Builder
    public OrderDetail(String userName, String tel, String email, String postcode, String deliveryAddress, String orderNumber, OrderStatus orderStatus, Member member, Long paymentPrice, String impId, Long deliveryPrice, String deliveryMsg, String orderName, String trackingNumber) {

        this.userName = userName;
        this.tel = tel;
        this.email = email;
        this.postcode = postcode;
        this.deliveryAddress = deliveryAddress;
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.member = member;
        this.paymentPrice = paymentPrice;
        this.impId = impId;
        this.deliveryPrice = deliveryPrice;
        this.deliveryMsg = deliveryMsg;
        this.orderName = orderName;
        this.trackingNumber = trackingNumber;
    }

    public void paid(PaymentResponse paymentResponse) {
        this.orderStatus = OrderStatus.PAID;
        this.userName = paymentResponse.getBuyerName();
        this.tel = paymentResponse.getBuyerTel();
        this.email = paymentResponse.getBuyerEmail();
        this.postcode = paymentResponse.getBuyerPostCode();
        this.deliveryAddress = paymentResponse.getBuyerAddress();
        this.impId = paymentResponse.getImpUID();
        this.orderName = paymentResponse.getName();
        this.deliveryPrice = paymentResponse.getDeliveryPrice();
    }

    public void updatePaymentPrice(Long paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public void updateOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updateTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

}
