package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_product", indexes = {
    @Index(name = "idx_product_order_number", columnList = "product_order_number", unique = true),
    @Index(name = "idx_tracking_number", columnList = "tracking_number")
})
@Inheritance(strategy = JOINED)
public class OrderProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;
    @Column(name = "order_status")
    @Enumerated(STRING)
    private OrderStatus orderStatus;
    private String name; // 주문 당시 구매한 상품 이름
    private String color;
    private String size;
    private Long count;
    @ColumnDefault("0")
    private Long price;
    @ColumnDefault("0")
    @Column(name = "total_price")
    private Long totalPrice;
    @Column(name = "tracking_number")
    private String trackingNumber;
    @Column(name = "product_order_number", unique = true)
    private Long productOrderNumber;
    @ColumnDefault("0")
    @Column(name = "cancel_amount")
    private Long cancelAmount;
    @ColumnDefault("0")
    @Column(name = "cancel_request_amount")
    private Long cancelRequestAmount;
    @Column(name = "cart_product_id")
    private Long cartProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductBase product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @Builder
    public OrderProduct(String name, String color, String size, Long count, Long price, Long totalPrice, Long productOrderNumber, ProductBase product, OrderDetail orderDetail, String trackingNumber, OrderStatus orderStatus, Long cancelAmount, Long cancelRequestAmount, Long cartProductId) {
        this.orderStatus = orderStatus;
        this.name = name;
        this.color = color;
        this.size = size;
        this.count = count;
        this.price = price;
        this.totalPrice = totalPrice;
        this.productOrderNumber = productOrderNumber;
        this.product = product;
        this.orderDetail = orderDetail;
        this.trackingNumber = trackingNumber;
        this.cancelAmount = cancelAmount;
        this.cancelRequestAmount = cancelRequestAmount;
        this.cartProductId = cartProductId;
    }

    public void updateTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void updateProductOrderNumber(Long productOrderNumber) {
        this.productOrderNumber = productOrderNumber;
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void addCancelAmount(Long cancelAmount) {
        this.cancelAmount += cancelAmount;
    }

    public void addCancelRequestAmount(Long cancelRequestAmount) {
        this.cancelRequestAmount += cancelRequestAmount;
    }

    public void initCancelRequestAmount() {
        this.cancelRequestAmount = 0L;
    }

    public void updateCartProductId(Long cartProductId) {
        this.cartProductId = cartProductId;
    }
}
