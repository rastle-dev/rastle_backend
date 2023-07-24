package rastle.dev.rastle_backend.domain.Orders.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Member.model.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "orders", catalog = "rastle_db")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
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
    private int orderNumber;

    @OneToMany(mappedBy = "orders", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<OrderProduct> orderProduct = new ArrayList<>();
}
