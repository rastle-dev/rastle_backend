package rastle.dev.rastle_backend.domain.Cart.api;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.Cart.application.CartService;
import rastle.dev.rastle_backend.domain.Cart.dto.CartDTO.CartItemInfoDto;
import rastle.dev.rastle_backend.domain.Cart.dto.CartDTO.CreateCartItemDto;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@Tag(name = "장바구니", description = "장바구니 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Operation(summary = "장바구니에 상품 추가", description = "장바구니에 상품을 추가합니다.")
    @ApiResponse(responseCode = "200", description = "장바구니에 상품 추가 성공")
    @FailApiResponses
    @PostMapping
    public ResponseEntity<ServerResponse<String>> addToCart(@RequestBody CreateCartItemDto createCartItemDto) {
        cartService.addToCart(createCartItemDto);
        ServerResponse<String> response = new ServerResponse<>("장바구니에 상품이 추가되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 조회", description = "장바구니에 담긴 상품을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "장바구니 상품 조회 성공", content = @Content(schema = @Schema(implementation = CartItemInfoDto.class)))
    @FailApiResponses
    @GetMapping
    public ResponseEntity<ServerResponse<Page<CartItemInfoDto>>> getCartItems(Pageable pageable) {
        Page<CartItemInfoDto> cartItems = cartService.getCartItems(pageable);
        ServerResponse<Page<CartItemInfoDto>> response = new ServerResponse<>(cartItems);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 내 모든 상품 삭제", description = "장바구니에 담긴 모든 상품을 삭제합니다.")
    @DeleteMapping("/removeAll")
    public ResponseEntity<ServerResponse<String>> removeAllProducts() {
        cartService.removeAllProducts();
        ServerResponse<String> response = new ServerResponse<>("장바구니에 담긴 모든 상품이 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니에서 선택한 상품 삭제", description = "장바구니에서 선택한 상품을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "장바구니에서 선택한 상품 삭제 성공")
    @FailApiResponses
    @DeleteMapping("/removeSelected")
    public ResponseEntity<ServerResponse<String>> removeSelectedProducts(@RequestParam List<Long> productIds) {
        cartService.removeSelectedProducts(productIds);
        ServerResponse<String> response = new ServerResponse<>("장바구니에서 선택한 상품이 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

}
