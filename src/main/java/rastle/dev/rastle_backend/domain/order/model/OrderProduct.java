package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_product")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;
    private String name; // 주문 당시 구매한 상품 이름
    private String color;
    private String size;
    private Long count;
    private Long totalPrice; // 구매한 상품 총 가격
    private String productOrderNumber;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductBase product;

    @ManyToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;
    @Builder
    public OrderProduct(String name, String color, String size, Long count, Long totalPrice, String productOrderNumber, ProductBase product, OrderDetail orderDetail) {
        this.name = name;
        this.color = color;
        this.size = size;
        this.count = count;
        this.totalPrice = totalPrice;
        this.productOrderNumber = productOrderNumber;
        this.product = product;
        this.orderDetail = orderDetail;
    }

    public void updateProductOrderNumber(String productOrderNumber) {
        this.productOrderNumber = productOrderNumber;
    }
}
