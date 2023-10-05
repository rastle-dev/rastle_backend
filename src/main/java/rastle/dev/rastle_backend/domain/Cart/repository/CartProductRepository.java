package rastle.dev.rastle_backend.domain.Cart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rastle.dev.rastle_backend.domain.Cart.dto.CartDTO.CartProductInfoDto;
import rastle.dev.rastle_backend.domain.Cart.model.Cart;
import rastle.dev.rastle_backend.domain.Product.model.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    void deleteAllByCart(Cart cart);

    void deleteByIdIn(List<Long> cartProductIds);

    @Query("SELECT new rastle.dev.rastle_backend.domain.Cart.dto.CartDTO$CartProductInfoDto(cp.id, p.name, p.price, cp.color, cp.size, cp.count, p.mainThumbnailImage) "
            +
            "FROM CartProduct cp " +
            "JOIN cp.product p " +
            "WHERE cp.cart.member.id = :memberId")
    Page<CartProductInfoDto> getCartProducts(@Param("memberId") Long memberId, Pageable pageable);
}
