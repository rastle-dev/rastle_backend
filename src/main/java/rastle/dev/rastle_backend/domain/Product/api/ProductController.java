package rastle.dev.rastle_backend.domain.Product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.Product.application.ProductService;
import rastle.dev.rastle_backend.domain.Product.dto.BundleProductInfo;
import rastle.dev.rastle_backend.domain.Product.dto.ColorInfo;
import rastle.dev.rastle_backend.domain.Product.dto.EventProductInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductImages;
import rastle.dev.rastle_backend.global.common.annotation.GetExecutionTime;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstant.ALL;

@Tag(name = "상품 관련 API", description = "상품 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "전체 상품 조회 API", description = "전체 상품 조회 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getProducts(
            @Parameter(name = "visible", description = "ALL - visible 여부 관계 없이 리턴, TRUE-true인 것만, FALSE - false인 것만")
            @RequestParam(name = "visible", defaultValue = ALL) String visible, Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductInfos(visible, pageable)));
    }

    @Operation(summary = "상품 세트 관련 상품 조회 API", description = "상품 세트 관련 상품 조회 API입니다. 상품 세트 조회 API를 먼저 호출하고 사용해야합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = BundleProductInfo.class)))
    @FailApiResponses
    @GetMapping("/bundle")
    public ResponseEntity<ServerResponse<?>> getBundleProducts(
            @Parameter(name = "visible", description = "ALL - visible 여부 관계 없이 리턴, TRUE-true인 것만, FALSE - false인 것만, 상품 세트 조회 api를 먼저 호출해야되고 그거랑 같은 값으로 넣어야함")
            @RequestParam(name = "visible", defaultValue = ALL) String visible,
            @Parameter(name = "lowerBound", description = "상품 세트 조회 api 호출해서 리턴된 최소 상품 세트 아이디", required = true)
            @RequestParam(name = "lowerBound") Long lowerBound,
            @Parameter(name = "upperBound", description = "상품 세트 조회 api 호출해서 리턴된 최대 상품 세트 아이디", required = true)
            @RequestParam(name = "upperBound") Long upperBound) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getBundleProducts(visible, lowerBound, upperBound)));
    }


    @Operation(summary = "이벤트 상품 조회 API", description = "이벤트 상품 조회 API입니다. 이벤트 조회 API를 먼저 호출하고 사용해야합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = EventProductInfo.class)))
    @FailApiResponses
    @GetMapping("/event")
    public ResponseEntity<ServerResponse<?>> getEventProducts(
            @Parameter(name = "visible", description = "ALL - visible 여부 관계 없이 리턴, TRUE-true인 것만, FALSE - false인 것만, 이벤트 조회 api를 먼저 호출해야되고 그거랑 같은 값으로 넣어야함")
            @RequestParam(name = "visible", defaultValue = ALL) String visible,
            @Parameter(name = "lowerBound", description = "이벤트 조회 api 호출해서 리턴된 최소 이벤트 아이디", required = true)
            @RequestParam(name = "lowerBound") Long lowerBound,
            @Parameter(name = "upperBound", description = "이벤트 조회 api 호출해서 리턴된 최대 이벤트 아이디", required = true)
            @RequestParam(name = "upperBound") Long upperBound) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getEventProducts(visible, lowerBound, upperBound)));
    }

    @Operation(summary = "상품 상세 조회 API", description = "상품 상세 조회 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    @FailApiResponses
    @GetMapping("/{id}/detail")
    public ResponseEntity<ServerResponse<?>> getProductDetail(
            @Parameter(name = "isEvent", description = "이벤트 상품인지 아닌지, 필수로 넘겨줘야함")
            @RequestParam(value = "isEvent", required = false) Boolean isEvent,
            @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductDetail(id, isEvent)));
    }

    @Operation(summary = "상품 색상 사이즈 조회 API", description = "상품 색상, 사이즈 조회 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = ColorInfo.class)))
    @FailApiResponses
    @GetMapping("/{id}/color")
    public ResponseEntity<ServerResponse<?>> getProductColor(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductColors(id)));
    }

    @Operation(summary = "상품 이미지 조회 API", description = "상품 이미지 조회 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = ProductImages.class)))
    @FailApiResponses
    @GetMapping("/{id}/image")
    public ResponseEntity<ServerResponse<?>> getProductImage(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductImages(id)));
    }
}
