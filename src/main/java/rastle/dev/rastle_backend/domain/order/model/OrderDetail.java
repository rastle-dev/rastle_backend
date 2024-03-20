package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rastle.dev.rastle_backend.domain.delivery.model.Delivery;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.payment.model.Payment;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;
//    @Column(name = "user_name")
//    private String userName;
    @Column(name = "order_name")
    private String orderName;
//    private String tel;
//    private String email;
//    private String postcode;
//    @Column(name = "delivery_address")
//    private String deliveryAddress;
    @Column(name = "order_number", unique = true)
    private String orderNumber;
    @Column(name = "product_price")
    private Long productPrice;
//    @Column(name = "payment_price")
//    private Long paymentPrice;
//    @Column(name = "delivery_price")
//    private Long deliveryPrice;
    @Enumerated(STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
//    @Column(name = "imp_id")
//    private String impId;
//    @Column(name = "delivery_msg")
//    private String deliveryMsg;
//    @Column(name = "tracking_number")
//    private String trackingNumber;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Setter
    @OneToOne(fetch = EAGER, cascade = ALL)
    private Delivery delivery;
    @Setter
    @OneToOne(fetch = EAGER, cascade = ALL)
    private Payment payment;

    @OneToMany(mappedBy = "orderDetail", fetch = FetchType.LAZY, cascade = REMOVE)
    private final List<CancelRequest> cancelRequests = new ArrayList<>();

    @OneToMany(mappedBy = "orderDetail", fetch = FetchType.LAZY, cascade = REMOVE)
    private final List<OrderProduct> orderProduct = new ArrayList<>();
    @Builder
    public OrderDetail(String orderName, String orderNumber, Long productPrice, OrderStatus orderStatus, Member member, Delivery delivery, Payment payment) {
        this.orderName = orderName;
        this.orderNumber = orderNumber;
        this.productPrice = productPrice;
        this.orderStatus = orderStatus;
        this.member = member;
        this.delivery = delivery;
        this.payment = payment;
    }

    //    @Builder
//    public OrderDetail(String userName, String tel, String email, String postcode, String deliveryAddress, String orderNumber, OrderStatus orderStatus, Member member, Long paymentPrice, String impId, Long deliveryPrice, String deliveryMsg, String orderName, String trackingNumber) {
//
//        this.userName = userName;
//        this.tel = tel;
//        this.email = email;
//        this.postcode = postcode;
//        this.deliveryAddress = deliveryAddress;
//        this.orderNumber = orderNumber;
//        this.orderStatus = orderStatus;
//        this.member = member;
//        this.paymentPrice = paymentPrice;
//        this.impId = impId;
//        this.deliveryPrice = deliveryPrice;
//        this.deliveryMsg = deliveryMsg;
//        this.orderName = orderName;
//        this.trackingNumber = trackingNumber;
//    }

    public void paid(PaymentResponse paymentResponse) {
        this.orderStatus = OrderStatus.PAID;
//        this.userName = paymentResponse.getBuyerName();
//        this.tel = paymentResponse.getBuyerTel();
//        this.email = paymentResponse.getBuyerEmail();
//        this.postcode = paymentResponse.getBuyerPostCode();
//        this.deliveryAddress = paymentResponse.getBuyerAddress();
//        this.impId = paymentResponse.getImpUID();
        this.orderName = paymentResponse.getName();
    }

//    public void updatePaymentPrice(Long paymentPrice) {
//        this.paymentPrice = paymentPrice;
//    }

    public void updateOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void updateProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }
//    public void updateTrackingNumber(String trackingNumber) {
//        this.trackingNumber = trackingNumber;
//    }

}
