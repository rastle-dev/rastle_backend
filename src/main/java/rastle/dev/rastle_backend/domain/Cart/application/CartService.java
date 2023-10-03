package rastle.dev.rastle_backend.domain.Cart.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.Cart.dto.CartDTO.CartItemInfoDto;
import rastle.dev.rastle_backend.domain.Cart.dto.CartDTO.CreateCartItemDto;
import rastle.dev.rastle_backend.domain.Cart.dto.CartDTO.DeleteCartItemDto;
import rastle.dev.rastle_backend.domain.Cart.model.Cart;
import rastle.dev.rastle_backend.domain.Cart.repository.CartProductRepository;
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
public class CartService {
        private final CartRepository cartRepository;
        private final CartProductRepository cartProductRepository;
        private final ProductBaseRepository productBaseRepository;
        private final MemberRepository memberRepository;

        /**
         * 장바구니에 상품 추가
         * 
         * @param cartItemInfoDto
         * @return void
         */
        @Transactional
        public void addToCart(List<CreateCartItemDto> createCartItemDtos) {
                Long memberId = SecurityUtil.getCurrentMemberId();

                Optional<Cart> cartOptional = cartRepository.findByMemberId(memberId);
                Cart cart = cartOptional.orElseGet(() -> {
                        Member member = memberRepository.findById(memberId)
                                        .orElseThrow(() -> new NotFoundByIdException());
                        return Cart.builder()
                                        .member(member)
                                        .build();
                });

                for (CreateCartItemDto createCartItemDto : createCartItemDtos) {
                        ProductBase product = productBaseRepository.findById(createCartItemDto.getProductId())
                                        .orElseThrow(() -> new NotFoundByIdException());

                        // 해당 상품이 장바구니에 이미 있는지 확인
                        Optional<CartProduct> existingCartProduct = cart.getCartProducts().stream()
                                        .filter(cp -> cp.getProduct().getId().equals(createCartItemDto.getProductId())
                                                        &&
                                                        cp.getColor().equals(createCartItemDto.getColor()) &&
                                                        cp.getSize().equals(createCartItemDto.getSize()))
                                        .findFirst();

                        if (existingCartProduct.isPresent()) {
                                // 장바구니에 이미 해당 상품이 있는 경우, 수량 추가
                                CartProduct cartProduct = existingCartProduct.get();
                                int updatedCount = cartProduct.getCount() + createCartItemDto.getCount();
                                cartProduct.updateCount(updatedCount);
                                cartProductRepository.save(cartProduct);
                        } else {
                                // 장바구니에 해당 상품이 없는 경우, 새로운 CartProduct 생성
                                CartProduct cartProduct = CartProduct.builder()
                                                .count(createCartItemDto.getCount())
                                                .color(createCartItemDto.getColor())
                                                .size(createCartItemDto.getSize())
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
         * @return List<CartItemInfoDto>
         */
        @Transactional
        public Page<CartItemInfoDto> getCartItems(Pageable pageable) {
                Long memberId = SecurityUtil.getCurrentMemberId();

                Cart cart = cartRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundByIdException());

                Page<CartItemInfoDto> cartItems = new PageImpl<>(
                                cart.getCartProducts().stream()
                                                .map(cp -> CartItemInfoDto.builder()
                                                                .productName(cp.getProduct().getName())
                                                                .productPrice(cp.getProduct().getPrice())
                                                                .color(cp.getColor())
                                                                .size(cp.getSize())
                                                                .count(cp.getCount())
                                                                .mainThumbnailImage(
                                                                                cp.getProduct().getMainThumbnailImage())
                                                                .build())
                                                .collect(Collectors.toList()),
                                pageable, cart.getCartProducts().size());

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
         * @param productIds
         * @return void
         */
        @Transactional
        public void removeSelectedProducts(List<DeleteCartItemDto> deleteCartItemDtos) {
                Long memberId = SecurityUtil.getCurrentMemberId();

                Cart cart = cartRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundByIdException());

                for (DeleteCartItemDto deleteCartItemDto : deleteCartItemDtos) {
                        Long productId = deleteCartItemDto.getProductId();
                        String color = deleteCartItemDto.getColor();
                        String size = deleteCartItemDto.getSize();

                        List<CartProduct> cartProductsToRemove = cart.getCartProducts()
                                        .stream()
                                        .filter(cp -> cp.getProduct().getId().equals(productId) &&
                                                        cp.getColor().equals(color) &&
                                                        cp.getSize().equals(size))
                                        .collect(Collectors.toList());

                        cart.getCartProducts().removeAll(cartProductsToRemove);
                        cartProductRepository.deleteAll(cartProductsToRemove);
                }

                cartRepository.save(cart);
        }
}
