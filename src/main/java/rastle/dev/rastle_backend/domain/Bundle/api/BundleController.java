package rastle.dev.rastle_backend.domain.Bundle.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.Bundle.application.BundleService;
import rastle.dev.rastle_backend.domain.Bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

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
    public ResponseEntity<ServerResponse<?>> getBundles(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(bundleService.getBundles(pageable)));
    }
}
