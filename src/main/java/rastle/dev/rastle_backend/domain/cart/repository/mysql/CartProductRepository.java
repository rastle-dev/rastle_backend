package rastle.dev.rastle_backend.domain.cart.repository.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.cart.dto.CartDTO.CartProductInfoDto;
import rastle.dev.rastle_backend.domain.cart.model.Cart;
import rastle.dev.rastle_backend.domain.product.model.CartProduct;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    void deleteAllByCart(Cart cart);

    void deleteByIdIn(List<Long> cartProductIds);

    @Query("SELECT new rastle.dev.rastle_backend.domain.cart.dto.CartDTO$CartProductInfoDto(cp.id, p.name, p.price, p.discountPrice, cp.color, cp.size, cp.count, p.mainThumbnailImage, p.id) "
            +
            "FROM CartProduct cp " +
            "JOIN cp.product p " +
            "WHERE cp.cart.member.id = :memberId")
    Page<CartProductInfoDto> getCartProducts(@Param("memberId") Long memberId, Pageable pageable);
}
