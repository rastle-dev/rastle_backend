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
import rastle.dev.rastle_backend.domain.Product.dto.ColorInfo;
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
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시"),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시"),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시")
    })
    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductInfos(pageable)));
    }

    @Operation(summary = "현재 마켓 상품 조회 API", description = "현재 마켓 상품 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시"),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시"),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시")
    })
    @GetMapping("/currentMarket")
    public ResponseEntity<ServerResponse<?>> getCurrentMarketProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getCurrentMarketProducts(pageable)));
    }

    @Operation(summary = "과거 마켓 상품 조회 API", description = "과거 마켓 상품 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시"),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시"),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시")
    })
    @GetMapping("/pastMarket")
    public ResponseEntity<ServerResponse<?>> getPastMarketProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getPastMarketProducts(pageable)));
    }

    @Operation(summary = "이벤트 상품 조회 API", description = "이벤트 상품 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시"),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시"),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시")
    })
    @GetMapping("/event")
    public ResponseEntity<ServerResponse<?>> getEventProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getEventMarketProducts(pageable)));
    }

    @Operation(summary = "상품 상세 조회 API", description = "상품 상세 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시"),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시"),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시")
    })
    @GetMapping("/{id}/detail")
    public ResponseEntity<ServerResponse<?>> getProductDetail(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductDetail(id)));
    }

    @Operation(summary = "상품 색상 사이즈 조회 API", description = "상품 색상, 사이즈 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = ColorInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시"),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시"),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시")
    })
    @GetMapping("/{id}/color")
    public ResponseEntity<ServerResponse<?>> getProductColor(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductColors(id)));
    }

    @Operation(summary = "상품 이미지 조회 API", description = "상품 이미지 조회 API 입니다")
    @ApiResponses(value = {

    })
    @GetMapping("/{id}/image")
    public ResponseEntity<ServerResponse<?>> getProductImage(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductImages(id)));
    }
}
