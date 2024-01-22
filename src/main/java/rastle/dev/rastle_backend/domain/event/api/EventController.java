package rastle.dev.rastle_backend.domain.event.api;

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
import rastle.dev.rastle_backend.domain.event.application.EventService;
import rastle.dev.rastle_backend.domain.event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO;
import rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO.EventProductApplyInfoDTO;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;
import rastle.dev.rastle_backend.global.util.SecurityUtil;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.ALL;

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
            @Parameter(name = "visible", description = "ALL - visible 여부 관계 없이 리턴, TRUE-true인 것만, FALSE - false인 것만") @RequestParam(name = "visible", defaultValue = ALL) String visible,
            Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(eventService.getEventInfo(visible, pageable)));
    }

    @Operation(summary = "이벤트에 속한 상품 조회 API", description = "이벤트에 속한 상품을 이벤트 아이디로 조회하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    @FailApiResponses
    @GetMapping("/{id}/products")
    public ResponseEntity<ServerResponse<?>> getEventProducts(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(eventService.getEventProducts(id)));
    }

    @Operation(summary = "이벤트 응모 신청 API", description = "이벤트 응모 신청을 합니다.")
    @ApiResponse(responseCode = "200", description = "신청 성공시", content = @Content(schema = @Schema(implementation = EventProductApplyDTO.class)))
    @FailApiResponses
    @PostMapping("/apply")
    public ResponseEntity<ServerResponse<?>> applyEventProduct(
            @RequestBody EventProductApplyDTO eventProductApplyDTO) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        eventService.applyEventProduct(currentMemberId, eventProductApplyDTO);
        return ResponseEntity.ok(new ServerResponse<>());
    }

    @Operation(summary = "이벤트 응모 신청 내역 조회 API", description = "이벤트 응모 신청 내역을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = EventProductApplyInfoDTO.class)))
    @FailApiResponses
    @GetMapping("/apply")
    public ResponseEntity<ServerResponse<?>> getEventProductApplyInfoDTOs() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(new ServerResponse<>(eventService.getEventProductApplyInfoDTOs(currentMemberId)));
    }
}
