package rastle.dev.rastle_backend.domain.cart.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.cart.dto.CartDTO.CartProductInfoDto;
import rastle.dev.rastle_backend.domain.cart.dto.CartDTO.CreateCartProductDto;
import rastle.dev.rastle_backend.domain.cart.model.Cart;
import rastle.dev.rastle_backend.domain.cart.repository.mysql.CartProductRepository;
import rastle.dev.rastle_backend.domain.cart.repository.mysql.CartRepository;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.domain.product.model.CartProduct;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductBaseRepository;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductBaseRepository productBaseRepository;
    private final MemberRepository memberRepository;

    /**
     * 장바구니에 상품 추가
     *
     * @param createCartProductDtos
     * @return void
     */
    @Transactional
    public void addToCart(List<CreateCartProductDto> createCartProductDtos) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Optional<Cart> cartOptional = cartRepository.findByMemberId(memberId);
        Cart cart = cartOptional.orElseGet(() -> {
            Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundByIdException());
            return Cart.builder()
                .member(member)
                .build();
        });

        for (CreateCartProductDto createCartProductDto : createCartProductDtos) {
            ProductBase product = productBaseRepository.findById(createCartProductDto.getProductId())
                .orElseThrow(() -> new NotFoundByIdException());

            // 해당 상품이 장바구니에 이미 있는지 확인
            Optional<CartProduct> existingCartProduct = cart.getCartProducts().stream()
                .filter(cp -> cp.getProduct().getId()
                    .equals(createCartProductDto.getProductId())
                    &&
                    cp.getColor().equals(createCartProductDto.getColor()) &&
                    cp.getSize().equals(createCartProductDto.getSize()))
                .findFirst();

            if (existingCartProduct.isPresent()) {
                // 장바구니에 이미 해당 상품이 있는 경우, 수량 누적 업데이트
                CartProduct cartProduct = existingCartProduct.get();
                int updatedCount = cartProduct.getCount() + createCartProductDto.getCount();
                cartProduct.updateCount(updatedCount);
                cartProductRepository.save(cartProduct);
            } else {
                // 장바구니에 해당 상품이 없는 경우, 새로운 CartProduct 생성
                CartProduct cartProduct = CartProduct.builder()
                    .count(createCartProductDto.getCount())
                    .color(createCartProductDto.getColor())
                    .size(createCartProductDto.getSize())
                    .cart(cart)
                    .product(product)
                    .build();
                cartProductRepository.save(cartProduct);
            }
        }

        cartRepository.save(cart);
    }

    /*
     * 장바구니 조회
     *
     * @return List<CartProductInfoDto>
     */
    @Transactional
    public Page<CartProductInfoDto> getCartProducts(Pageable pageable) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Page<CartProductInfoDto> cartItems = cartProductRepository.getCartProducts(memberId, pageable);

        return cartItems;
    }

    /*
     * 장바구니에서 모든 상품 삭제
     *
     * @return void
     */
    @Transactional
    public void removeAllProducts() {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Cart cart = cartRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundByIdException());

        cart.getCartProducts().clear();
        cartRepository.save(cart);

        cartProductRepository.deleteAllByCart(cart);
    }

    /**
     * 장바구니에서 선택한 상품들 삭제
     *
     * @param deleteCartProductIdList
     * @return void
     */
    @Transactional
    public void removeSelectedProducts(List<Long> deleteCartProductIdList) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Cart cart = cartRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundByIdException());

        cart.getCartProducts().removeIf(cp -> deleteCartProductIdList.contains(cp.getId()));

        cartProductRepository.deleteByIdIn(deleteCartProductIdList);
        cartRepository.save(cart);
    }
}
