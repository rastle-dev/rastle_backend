package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rastle.dev.rastle_backend.domain.delivery.model.Delivery;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.payment.model.Payment;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.common.enums.CartProductStatus;
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
    @Column(name = "order_name")
    private String orderName;
    @Column(name = "order_number", unique = true)
    private Long orderNumber;
    @Column(name = "order_price")
    private Long orderPrice;
    @Enumerated(STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @Setter
    @OneToOne(mappedBy = "orderDetail", fetch = EAGER, cascade = ALL)
    private Delivery delivery;
    @Setter
    @OneToOne(mappedBy = "orderDetail", fetch = EAGER, cascade = ALL)
    private Payment payment;

    @OneToMany(mappedBy = "orderDetail", fetch = FetchType.LAZY, cascade = REMOVE)
    private final List<CancelRequest> cancelRequests = new ArrayList<>();

    @OneToMany(mappedBy = "orderDetail", fetch = FetchType.LAZY, cascade = REMOVE)
    private final List<OrderProduct> orderProduct = new ArrayList<>();

    @Builder
    public OrderDetail(String orderName, Long orderNumber, Long orderPrice, OrderStatus orderStatus, Member member, Delivery delivery, Payment payment) {
        this.orderName = orderName;
        this.orderNumber = orderNumber;
        this.orderPrice = orderPrice;
        this.orderStatus = orderStatus;
        this.member = member;
        this.delivery = delivery;
        this.payment = payment;
    }


    public void paid(PaymentResponse paymentResponse) {
        this.orderStatus = OrderStatus.PAID;
        this.orderName = paymentResponse.getName();
        this.getPayment().paid(paymentResponse);
        this.getDelivery().paid(paymentResponse);

        for (OrderProduct orderProduct : this.getOrderProduct()) {
            ProductBase product = orderProduct.getProduct();
            orderProduct.updateOrderStatus(OrderStatus.PAID);
            product.incrementSoldCount();
            if (orderProduct.getCartProduct() != null) {
                orderProduct.getCartProduct().updateCartProductStatus(CartProductStatus.ORDERED);
            }
        }
    }


    public void updateOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }



    public void updateOrderPrice(Long orderPrice) {
        this.orderPrice = orderPrice;
    }


}
