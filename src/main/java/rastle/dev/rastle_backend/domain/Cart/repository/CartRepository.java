package rastle.dev.rastle_backend.domain.Cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Cart.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByMemberId(Long memberId);
}
