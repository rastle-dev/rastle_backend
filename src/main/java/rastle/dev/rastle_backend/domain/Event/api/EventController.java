package rastle.dev.rastle_backend.domain.Event.api;

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
import rastle.dev.rastle_backend.domain.Event.application.EventService;
import rastle.dev.rastle_backend.domain.Event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstant.ALL;

@Tag(name = "이벤트 관련 API", description = "이벤트 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @Operation(summary = "이벤트 조회 API", description = "이벤트 조회 API 입니다")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = EventInfo.class)))
    @FailApiResponses
    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getEventDetail(
            @Parameter(name = "상품 세트 가시성 여부", description = "ALL - visible 여부 관계 없이 리턴, TRUE-true인 것만, FALSE - false인 것만")
            @RequestParam(name = "visible", defaultValue = ALL) String visible,
            Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(eventService.getEventInfo(visible, pageable)));
    }
}
