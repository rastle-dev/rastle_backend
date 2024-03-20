package rastle.dev.rastle_backend.domain.delivery.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;

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
    private String address;
    private String postcode;
    private String email;
    private String tel;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "delivery_price")
    private Long deliveryPrice;
    @Column(name = "island_delivery_price")
    private Long islandDeliveryPrice;
    private String msg;
    @Column(name = "tracking_number")
    private String trackingNumber;
    @OneToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @Builder
    public Delivery(String address, String postcode, String email, String tel, String userName, Long deliveryPrice, Long islandDeliveryPrice, String msg, String trackingNumber, OrderDetail orderDetail) {
        this.address = address;
        this.postcode = postcode;
        this.email = email;
        this.tel = tel;
        this.userName = userName;
        this.deliveryPrice = deliveryPrice;
        this.islandDeliveryPrice = islandDeliveryPrice;
        this.msg = msg;
        this.trackingNumber = trackingNumber;
        this.orderDetail = orderDetail;
    }

    public void paid(PaymentResponse paymentResponse) {
        this.address = paymentResponse.getBuyerAddress();
        this.postcode = paymentResponse.getBuyerPostCode();
        this.email = paymentResponse.getBuyerEmail();
        this.tel = paymentResponse.getBuyerTel();
        this.userName = paymentResponse.getBuyerName();
        this.deliveryPrice = paymentResponse.getDeliveryPrice();
        this.islandDeliveryPrice = paymentResponse.getIslandDeliveryPrice();
        this.msg = paymentResponse.getDeliveryMsg();
    }

    public void updateTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

}
