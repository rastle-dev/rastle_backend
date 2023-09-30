package rastle.dev.rastle_backend.domain.Cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rastle.dev.rastle_backend.domain.Cart.model.Cart;
import rastle.dev.rastle_backend.domain.Product.model.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    void deleteAllByCart(Cart cart);
}
