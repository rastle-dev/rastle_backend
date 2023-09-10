package rastle.dev.rastle_backend.domain.Market.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.Market.application.MarketService;
import rastle.dev.rastle_backend.domain.Market.dto.MarketDTO;
import rastle.dev.rastle_backend.domain.Market.dto.MarketDTO.MarketCreateRequest;
import rastle.dev.rastle_backend.domain.Market.dto.MarketInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@Tag(name = "마켓 관련 API", description = "마켓 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {
    private final MarketService marketService;



    @Operation(summary = "마켓 조회 API", description = "마켓 조회 API 입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = MarketInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getProductDetail() {
        return ResponseEntity.ok(new ServerResponse<>(marketService.getCurrentMarkets()));
    }
}
