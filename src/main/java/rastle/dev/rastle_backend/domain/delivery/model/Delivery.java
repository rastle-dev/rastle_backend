package rastle.dev.rastle_backend.domain.delivery.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.common.enums.DeliveryStatus;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "delivery")
@Inheritance(strategy = JOINED)
public class Delivery extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "delivery_id")
    private Long id;
    @Enumerated(STRING)
    private DeliveryStatus status;
    private String address;
    private String email;
    private String postcode;
    private String tel;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "delivery_price")
    private Long deliveryPrice;
    private String msg;

    @Builder
    public Delivery(DeliveryStatus status, String address, String email, String postcode, String tel, String userName, Long deliveryPrice, String msg) {
        this.status = status;
        this.address = address;
        this.email = email;
        this.postcode = postcode;
        this.tel = tel;
        this.userName = userName;
        this.deliveryPrice = deliveryPrice;
        this.msg = msg;
    }
}
