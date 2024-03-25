package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;

import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_product")
@Inheritance(strategy = JOINED)
public class OrderProduct extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;
    private String name; // 주문 당시 구매한 상품 이름
    private String color;
    private String size;
    private Long count;
    @ColumnDefault("0")
    private Long price;
    @Column(name = "total_price")
    @ColumnDefault("0")
    private Long totalPrice; // 구매한 상품 총 가격
    @Column(name = "product_order_number", unique = true)
    private Long productOrderNumber;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductBase product;

    @ManyToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @Builder
    public OrderProduct(String name, String color, String size, Long count, Long price, Long totalPrice, Long productOrderNumber, ProductBase product, OrderDetail orderDetail) {
        this.name = name;
        this.color = color;
        this.size = size;
        this.count = count;
        this.price = price;
        this.totalPrice = totalPrice;
        this.productOrderNumber = productOrderNumber;
        this.product = product;
        this.orderDetail = orderDetail;
    }

    public void updateProductOrderNumber(Long productOrderNumber) {
        this.productOrderNumber = productOrderNumber;
    }
}
