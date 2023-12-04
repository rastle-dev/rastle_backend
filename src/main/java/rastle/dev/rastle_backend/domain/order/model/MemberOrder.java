package rastle.dev.rastle_backend.domain.order.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.global.common.BaseTimeEntity;
import rastle.dev.rastle_backend.global.common.enums.DeliveryStatus;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.InheritanceType.JOINED;

/**
 * 주문이 완료된 데이터를 관리하는 객체
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "member_order")
@Inheritance(strategy = JOINED)
public class MemberOrder extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "user_name")
    private String userName;

    private String email;
    private int zipcode;
    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "detailed_address")
    private String detailedAddress;

    @Column(name = "order_number")
    private String orderNumber;
    @Enumerated(STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @OneToMany(mappedBy = "memberOrder", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<OrderProduct> orderProduct = new ArrayList<>();
}
