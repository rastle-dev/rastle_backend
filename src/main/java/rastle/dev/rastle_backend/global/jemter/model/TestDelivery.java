package rastle.dev.rastle_backend.global.jemter.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_delivery")
public class TestDelivery {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String address;
    private String postcode;
    private String email;
    private String tel;
    @Column(name = "user_name")
    private String userName;
    @ColumnDefault("0")
    @Column(name = "delivery_price")
    private Long deliveryPrice;
    @ColumnDefault("0")
    @Column(name = "island_delivery_price")
    private Long islandDeliveryPrice;
    private String msg;
    private String deliveryService;

}
