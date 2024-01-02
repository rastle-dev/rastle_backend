package rastle.dev.rastle_backend.domain.coupon.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rastle.dev.rastle_backend.domain.coupon.application.CouponService;
import rastle.dev.rastle_backend.domain.coupon.dto.CouponResponseDTO;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@Tag(name = "쿠폰 정보", description = "쿠폰 정보 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {
    private final CouponService couponService;

    @Operation(summary = "멤버 쿠폰 조회 API", description = "멤버 쿠폰 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = CouponResponseDTO.class)))
    @FailApiResponses
    @GetMapping()
    public ResponseEntity<ServerResponse<?>> getMemberCoupons() {
        return ResponseEntity.ok(new ServerResponse<>(couponService.getMemberCoupons()));
    }
}
