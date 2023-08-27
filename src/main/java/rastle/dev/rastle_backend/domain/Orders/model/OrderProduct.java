package rastle.dev.rastle_backend.domain.Orders.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Product.model.ProductBase;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "order_product", catalog = "rastle_db")
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    private Long id;

    private String color;
    private String size;
    private int count;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductBase product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders orders;

}
