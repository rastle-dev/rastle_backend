package rastle.dev.rastle_backend.domain.Product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.Product.application.ProductService;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductCreateRequest;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@Tag(name = "상품 관련 API", description = "상품 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    @PostMapping("")
    public ResponseEntity<ServerResponse<?>> tempProductSave(@RequestBody ProductCreateRequest createRequest) {
        return ResponseEntity.ok(new ServerResponse<>(productService.createProduct(createRequest)));
    }

    @Operation(summary = "전체 상품 조회 API", description = "전체 상품 조회 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    })
    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductInfos(pageable)));
    }

    @Operation(summary = "현재 마켓 상품 조회 API", description = "현재 마켓 상품 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    })
    @GetMapping("/currentMarket")
    public ResponseEntity<ServerResponse<?>> getCurrentMarketProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getCurrentMarketProducts(pageable)));
    }

    @Operation(summary = "과거 마켓 상품 조회 API", description = "과거 마켓 상품 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    })
    @GetMapping("/pastMarket")
    public ResponseEntity<ServerResponse<?>> getPastMarketProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getPastMarketProducts(pageable)));
    }

    @Operation(summary = "이벤트 상품 조회 API", description = "이벤트 상품 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    })
    @GetMapping("/event")
    public ResponseEntity<ServerResponse<?>> getEventProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getEventMarketProducts(pageable)));
    }

    @Operation(summary = "상품 상세 조회 API", description = "상품 상세 조회 API 입니다")
    @GetMapping("/{id}/detail")
    public ResponseEntity<ServerResponse<?>> getProductDetail(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductDetail(id)));
    }

    @Operation(summary = "상품 색상 사이즈 조회 API", description = "상품 색상, 사이즈 조회 API 입니다")
    @GetMapping("/{id}/color")
    public ResponseEntity<ServerResponse<?>> getProductColor(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductColors(id)));
    }

    @Operation(summary = "상품 이미지 조회 API", description = "상품 이미지 조회 API 입니다")
    @GetMapping("/{id}/image")
    public ResponseEntity<ServerResponse<?>> getProductImage(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductImages(id)));
    }
}
