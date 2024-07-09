package rastle.dev.rastle_backend.domain.hibernate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestCartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id", foreignKey = @ForeignKey(name = "FK_cart_product_member"))
    private TestMember member;

    @ManyToOne
    @JoinColumn(name="product_id", foreignKey = @ForeignKey(name = "FK_cart_product_product"))
    private TestProduct product;

    private Long amount;
}
