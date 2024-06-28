package rastle.dev.rastle_backend.domain.cart.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.global.common.enums.CartProductStatus;

import static jakarta.persistence.EnumType.STRING;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductBase product;
    @Column(name = "cart_product_status")
    @Enumerated(STRING)
    private CartProductStatus cartProductStatus;
    @Builder
    public CartProduct(Long id, int count, String color, String size, Cart cart, ProductBase product, CartProductStatus cartProductStatus) {
        this.id = id;
        this.count = count;
        this.color = color;
        this.size = size;
        this.cart = cart;
        this.product = product;
        this.cartProductStatus = cartProductStatus;
    }

    public void updateCount(int count) {
        this.count = count;
    }

    public void updateCartProductStatus(CartProductStatus cartProductStatus) {
        this.cartProductStatus = cartProductStatus;
    }
}
