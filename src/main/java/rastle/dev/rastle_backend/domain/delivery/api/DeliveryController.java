package rastle.dev.rastle_backend.domain.delivery.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rastle.dev.rastle_backend.domain.delivery.application.DeliveryService;
import rastle.dev.rastle_backend.domain.delivery.dto.request.WebHookRequest;

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


    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebHook(
        @RequestBody WebHookRequest webHookRequest
    ) {
        return ResponseEntity.accepted().body(deliveryService.handleWebHook(webHookRequest));
    }

}
