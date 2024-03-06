package rastle.dev.rastle_backend.domain.product.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.cart.model.Cart;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cart_product")
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_product_id")
    private Long id;

    private int count;
    private String color;
    private String size;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductBase product;

    @Builder
    public CartProduct(int count, String color, String size, Cart cart, ProductBase product) {
        this.count = count;
        this.color = color;
        this.size = size;
        this.cart = cart;
        this.product = product;
    }

    public void updateCount(int count) {
        this.count = count;
    }
}
