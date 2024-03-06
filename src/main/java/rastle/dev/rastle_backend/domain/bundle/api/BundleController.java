package rastle.dev.rastle_backend.domain.bundle.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.bundle.application.BundleService;
import rastle.dev.rastle_backend.domain.bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.ALL;

@Tag(name = "상품 세트 관련 API", description = "상품 세트 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/bundle")
public class BundleController {
    private final BundleService bundleService;

    @Operation(summary = "상품 세트 조회 API", description = "상품 세트 조회 API 입니다")
    @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = BundleInfo.class)))
    @FailApiResponses
    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getBundles(
        @Parameter(name = "visible", description = "ALL - visible 여부 관계 없이 리턴, TRUE-true인 것만, FALSE - false인 것만")
        @RequestParam(name = "visible", defaultValue = ALL) String visible,
        Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(bundleService.getBundles(visible, pageable)));
    }

    @Operation(summary = "상품 세트에 속한 상품 조회 API", description = "상품 세트에 속한 상품을 상품 세트 아이디로 조회하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    @FailApiResponses
    @GetMapping("/{id}/products")
    public ResponseEntity<ServerResponse<?>> getBundleProducts(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(bundleService.getBundleProducts(id)));
    }
}
