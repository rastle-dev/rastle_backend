package rastle.dev.rastle_backend.domain.Product.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.Product.application.ProductService;
import rastle.dev.rastle_backend.domain.Product.dto.ColorInfo;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductCreateRequest;
import rastle.dev.rastle_backend.domain.Product.dto.ProductImageInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.common.annotation.GetExecutionTime;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import java.io.IOException;
import java.util.List;

@Tag(name = "상품 관련 API", description = "상품 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Slf4j
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 생성 API", description = "상품 생성 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetExecutionTime
    @PostMapping("")
    public ResponseEntity<ServerResponse<?>> createProduct(@RequestBody ProductCreateRequest createRequest) throws IOException {

        return ResponseEntity.ok(new ServerResponse<>(productService.createProduct(createRequest)));
    }

    @Operation(summary = "상품 메인 썸네일 이미지 등록 API", description = "상품 메인 썸네일 이미지 등록 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = ProductImageInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetExecutionTime
    @PostMapping("/{id}/mainThumbnail")
    public ResponseEntity<ServerResponse<?>> uploadMainThumbnail(@PathVariable("id") Long id,
                                                                @RequestParam("mainThumbnail") MultipartFile mainThumbnail) {
        return ResponseEntity.ok(new ServerResponse<>(productService.uploadMainThumbnail(id, mainThumbnail)));

    }


    @Operation(summary = "상품 서브 썸네일 이미지 등록 API", description = "상품 서브 썸네일 이미지 등록 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = ProductImageInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetExecutionTime
    @PostMapping("/{id}/subThumbnail")
    public ResponseEntity<ServerResponse<?>> uploadSubThumbnail(@PathVariable("id") Long id,
                                                                @RequestParam("subThumbnail") MultipartFile subThumbnail) {
        return ResponseEntity.ok(new ServerResponse<>(productService.uploadSubThumbnail(id, subThumbnail)));

    }

    @Operation(summary = "상품 메인 이미지들 등록 API", description = "상품 메인 이미지들 등록 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = ProductImageInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetExecutionTime
    @PostMapping("/{id}/mainImages")
    public ResponseEntity<ServerResponse<?>> uploadMainImages(@PathVariable("id") Long id,
                                                                @RequestParam("mainImages") List<MultipartFile> mainImages) {
        return ResponseEntity.ok(new ServerResponse<>(productService.uploadMainImages(id, mainImages)));

    }

    @Operation(summary = "상품 상세 이미지들 등록 API", description = "상품 상세 이미지들 등록 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = ProductImageInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetExecutionTime
    @PostMapping("/{id}/detailImages")
    public ResponseEntity<ServerResponse<?>> uploadDetailImages(@PathVariable("id") Long id,
                                                              @RequestParam("detailImages") List<MultipartFile> detailImages) {
        return ResponseEntity.ok(new ServerResponse<>(productService.uploadDetailImages(id, detailImages)));

    }

    @Operation(summary = "전체 상품 조회 API", description = "전체 상품 조회 API 입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetExecutionTime
    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductInfos(pageable)));
    }

    @Operation(summary = "현재 마켓 상품 조회 API", description = "현재 마켓 상품 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/currentMarket")
    public ResponseEntity<ServerResponse<?>> getCurrentMarketProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getCurrentMarketProducts(pageable)));
    }

    @Operation(summary = "과거 마켓 상품 조회 API", description = "과거 마켓 상품 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/pastMarket")
    public ResponseEntity<ServerResponse<?>> getPastMarketProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getPastMarketProducts(pageable)));
    }

    @Operation(summary = "이벤트 상품 조회 API", description = "이벤트 상품 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/event")
    public ResponseEntity<ServerResponse<?>> getEventProducts(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getEventMarketProducts(pageable)));
    }

    @Operation(summary = "상품 상세 조회 API", description = "상품 상세 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/detail")
    public ResponseEntity<ServerResponse<?>> getProductDetail(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(productService.getProductDetail(id)));
    }

    @Operation(summary = "상품 색상 사이즈 조회 API", description = "상품 색상, 사이즈 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = ColorInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
