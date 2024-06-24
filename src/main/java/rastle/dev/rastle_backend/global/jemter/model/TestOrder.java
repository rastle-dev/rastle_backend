package rastle.dev.rastle_backend.global.jemter.model;

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
    @Column(name = "order_number", unique = true)
    private Long orderNumber;
    @Column(name = "order_price")
    private Long orderPrice;
    @Enumerated(STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

}
