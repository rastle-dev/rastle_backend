package rastle.dev.rastle_backend.domain.cart.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.cart.application.CartService;
import rastle.dev.rastle_backend.domain.cart.dto.CartDTO.CartProductInfoDto;
import rastle.dev.rastle_backend.domain.cart.dto.CartDTO.CreateCartProductDto;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import java.util.List;

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
    public ResponseEntity<ServerResponse<String>> addToCart(
        @RequestBody List<CreateCartProductDto> createCartProductDtos) {
        cartService.addToCart(createCartProductDtos);
        ServerResponse<String> response = new ServerResponse<>("장바구니에 상품이 추가되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 조회", description = "장바구니에 담긴 상품을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "장바구니 상품 조회 성공", content = @Content(schema = @Schema(implementation = CartProductInfoDto.class)))
    @FailApiResponses
    @GetMapping
    public ResponseEntity<ServerResponse<Page<CartProductInfoDto>>> getCartProducts(Pageable pageable) {
        Page<CartProductInfoDto> cartProducts = cartService.getCartProducts(pageable);
        ServerResponse<Page<CartProductInfoDto>> response = new ServerResponse<>(cartProducts);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니 내 모든 상품 삭제", description = "장바구니에 담긴 모든 상품을 삭제합니다.")
    @DeleteMapping
    public ResponseEntity<ServerResponse<String>> removeAllProducts() {
        cartService.removeAllProducts();
        ServerResponse<String> response = new ServerResponse<>("장바구니에 담긴 모든 상품이 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "장바구니에서 선택한 상품 삭제", description = "장바구니에서 선택한 상품을 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "장바구니에서 선택한 상품 삭제 성공")
    @FailApiResponses
    @DeleteMapping("/removeSelected")
    public ResponseEntity<ServerResponse<String>> removeSelectedProducts(
        @RequestParam List<Long> deleteCartProductIdList) {
        cartService.removeSelectedProducts(deleteCartProductIdList);
        ServerResponse<String> response = new ServerResponse<>("장바구니에서 선택한 상품이 삭제되었습니다.");
        return ResponseEntity.ok(response);
    }
}
