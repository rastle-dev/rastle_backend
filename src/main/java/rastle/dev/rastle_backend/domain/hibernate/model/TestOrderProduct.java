package rastle.dev.rastle_backend.domain.hibernate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestOrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="product_id", foreignKey = @ForeignKey(name = "FK_order_product_product"))
    private TestProduct product;
    @ManyToOne
    @JoinColumn(name="order_id", foreignKey = @ForeignKey(name = "FK_order_product_order"))
    private TestOrder order;

    private Long amount;
}
