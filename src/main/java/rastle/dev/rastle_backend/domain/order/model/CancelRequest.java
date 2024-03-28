package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.common.enums.CancelRequestStatus;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static jakarta.persistence.InheritanceType.JOINED;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cancel_request")
@Inheritance(strategy = JOINED)
public class CancelRequest extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cancel_request_id")
    private Long id;

    private String reason;
    @Column(name = "product_order_number")
    private Long productOrderNumber;
    @Column(name = "cancel_request_status")
    @Enumerated(STRING)
    private CancelRequestStatus cancelRequestStatus;

    @ManyToOne
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;


    @Builder
    public CancelRequest(String reason, Long productOrderNumber, OrderDetail orderDetail, CancelRequestStatus cancelRequestStatus) {
        this.reason = reason;
        this.productOrderNumber = productOrderNumber;
        this.orderDetail = orderDetail;
        this.cancelRequestStatus = cancelRequestStatus;
    }

    public void updateStatus(CancelRequestStatus cancelRequestStatus) {
        this.cancelRequestStatus = cancelRequestStatus;

    }
}
