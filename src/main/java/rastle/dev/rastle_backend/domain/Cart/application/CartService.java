package rastle.dev.rastle_backend.domain.Cart.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rastle.dev.rastle_backend.domain.Cart.dto.CartDTO.CartItemInfoDto;
import rastle.dev.rastle_backend.domain.Cart.dto.CartDTO.CreateCartItemDto;
import rastle.dev.rastle_backend.domain.Cart.model.Cart;
import rastle.dev.rastle_backend.domain.Cart.repository.CartRepository;
import rastle.dev.rastle_backend.domain.Member.model.Member;
import rastle.dev.rastle_backend.domain.Member.repository.MemberRepository;
import rastle.dev.rastle_backend.domain.Product.model.CartProduct;
import rastle.dev.rastle_backend.domain.Product.model.ProductBase;
import rastle.dev.rastle_backend.domain.Product.repository.ProductBaseRepository;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.util.SecurityUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final ProductBaseRepository productBaseRepository;
    private final MemberRepository memberRepository;

    /**
     * 장바구니에 상품 추가
     * 
     * @param cartItemInfoDto
     * @return void
     */
    @Transactional
    public void addToCart(CreateCartItemDto createCartItemDto) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Optional<Cart> cartOptional = cartRepository.findByMemberId(memberId);
        Cart cart = cartOptional.orElseGet(() -> {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new NotFoundByIdException());
            return Cart.builder()
                    .member(member)
                    .build();
        });

        log.info(createCartItemDto.getProductId().toString());
        ProductBase product = productBaseRepository.findById(createCartItemDto.getProductId())
                .orElseThrow(() -> new NotFoundByIdException());

        // 해당 상품이 장바구니에 이미 있는지 확인
        Optional<CartProduct> existingCartProduct = cart.getCartProducts().stream()
                .filter(cp -> cp.getProduct().getId().equals(createCartItemDto.getProductId()))
                .findFirst();

        if (existingCartProduct.isPresent()) {
            // 장바구니에 이미 해당 상품이 있는 경우, 수량 업데이트
            CartProduct cartProduct = existingCartProduct.get();
            cartProduct.updateCount(createCartItemDto.getCount());
        } else {
            // 장바구니에 해당 상품이 없는 경우, 새로운 CartProduct 생성
            CartProduct cartProduct = CartProduct.builder()
                    .count(createCartItemDto.getCount())
                    .color(createCartItemDto.getColor())
                    .size(createCartItemDto.getSize())
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getCartProducts().add(cartProduct);
        }

        cartRepository.save(cart);
    }

    /*
     * 장바구니 조회
     * 
     * @return List<CartItemInfoDto>
     */
    public List<CartItemInfoDto> getCartItems() {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Cart cart = cartRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundByIdException());

        List<CartItemInfoDto> cartItems = cart.getCartProducts()
                .stream()
                .map(cp -> {
                    CartItemInfoDto dto = CartItemInfoDto.builder()
                            .productName(cp.getProduct().getName())
                            .productPrice(cp.getProduct().getPrice())
                            .color(cp.getColor())
                            .size(cp.getSize())
                            .count(cp.getCount())
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());

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
    }

    /**
     * 장바구니에서 선택한 상품들 삭제
     * 
     * @param productIds
     * @return void
     */
    @Transactional
    public void removeSelectedProducts(List<Long> productIds) {
        Long memberId = SecurityUtil.getCurrentMemberId();

        Cart cart = cartRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundByIdException());

        List<CartProduct> cartProductsToRemove = cart.getCartProducts()
                .stream()
                .filter(cp -> productIds.contains(cp.getProduct().getId()))
                .collect(Collectors.toList());

        cart.getCartProducts().removeAll(cartProductsToRemove);
        cartRepository.save(cart);
    }
}
