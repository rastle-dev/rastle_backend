package rastle.dev.rastle_backend.domain.delivery.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.delivery.application.DeliveryService;
import rastle.dev.rastle_backend.domain.delivery.dto.request.WebHookRequest;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;

@Tag(name = "배송 기능 API", description = "배송 기능 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;
    /*
    배송 처리 흐름
    관리자가 운송장 번호 입력하면서 웹훅 등록 api 호출
    웹훅 요청 받으면, 사용자에게 배송 상태 변경 정보 안내해줘야할듯
    + batch로 배송 완료되지 않은 요청들은 하루에 한번 웹훅 만료 시간 갱신해줘야할듯?
     */

    @Operation(summary = "운송장 번호 웹훅 등록 API", description = "운송장 번호 웹훅 등록 api 입니다.")
    @PostMapping("/{trackingNumber}")
    public ResponseEntity<ServerResponse<String>> registerWebhook(
        @Parameter(name = "trackingNumber", description = "운송장 번호", required = true, in = PATH)
        @PathVariable("trackingNumber")
        String trackingNumber
    ) {
        return ResponseEntity.ok(new ServerResponse<>(deliveryService.registerWebHook(trackingNumber)));
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebHook(
        @RequestBody WebHookRequest webHookRequest
    ) {
        return ResponseEntity.accepted().body(deliveryService.handleWebHook(webHookRequest));
    }

}
