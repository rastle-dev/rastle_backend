package rastle.dev.rastle_backend.domain.hibernate.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestOrder {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String orderNumber;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<TestOrderProduct> orderProducts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @jakarta.persistence.ForeignKey(name = "FK_order_member"))
    private TestMember member;

    @OneToOne(mappedBy = "order")
    private TestPayment payment;

    @OneToOne(mappedBy = "order")
    private TestDelivery delivery;

}
