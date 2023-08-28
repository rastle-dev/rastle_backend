package rastle.dev.rastle_backend.domain.Market.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.Market.application.MarketService;
import rastle.dev.rastle_backend.domain.Market.dto.MarketDTO;
import rastle.dev.rastle_backend.domain.Market.dto.MarketDTO.MarketCreateRequest;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@Tag(name = "마켓 관련 API", description = "마켓 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/market")
public class MarketController {
    private final MarketService marketService;

    @Operation(summary = "마켓 생성 API", description = "마켓 생성 API입니다")
    @PostMapping("")
    public ResponseEntity<ServerResponse<?>> tempAddMarket(@RequestBody MarketCreateRequest createRequest) {
        return ResponseEntity.ok(new ServerResponse<>(marketService.createMarket(createRequest)));
    }

    @Operation(summary = "마켓 조회 API", description = "마켓 조회 API 입니다")
    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getProductDetail() {
        return ResponseEntity.ok(new ServerResponse<>(marketService.getCurrentMarkets()));
    }
}
