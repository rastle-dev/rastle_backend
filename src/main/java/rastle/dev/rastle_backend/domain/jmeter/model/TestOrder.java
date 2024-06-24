package rastle.dev.rastle_backend.domain.jmeter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_order")
public class TestOrder {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @Column(name = "order_name")
    private String orderName;
    @Column(name = "order_number")
    private Long orderNumber;
    @Column(name = "order_price")
    private Long orderPrice;
    @Enumerated(STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    public static TestOrder newOrder() {
        return TestOrder.builder()
            .orderName("12341234")
            .orderPrice(12340123L)
            .orderStatus(OrderStatus.DELIVERED)
            .build();
    }

}
