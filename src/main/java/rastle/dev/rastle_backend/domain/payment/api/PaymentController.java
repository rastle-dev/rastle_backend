package rastle.dev.rastle_backend.domain.payment.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import rastle.dev.rastle_backend.domain.payment.application.PaymentService;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentPrepareRequest;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentVerificationRequest;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentVerificationResponse;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneWebHookRequest;
import rastle.dev.rastle_backend.domain.payment.exception.PaymentErrorException;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import java.net.URI;

@Tag(name = "결제 API", description = "결제 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "결제 사후 검증 및 생성 API", description = "포트원 API에서 발생한 결제 요청을 검증하고, memberOrder를 생성하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "검증 성공시", content = @Content(schema = @Schema(implementation = PaymentVerificationResponse.class)))
    @PostMapping("/complete")
    public ResponseEntity<?> verifyPaymentCompletion(
            @RequestBody PaymentVerificationRequest paymentVerificationRequest) {
        return ResponseEntity.ok(new ServerResponse<>(paymentService.verifyPayment(paymentVerificationRequest)));
    }

    @Operation(summary = "모바일 결제 사후 검증 및 생성 API", description = "포트원 API에서 발생한 모바일 결제 요청을 검증하고, memberOrder를 생성한 후 리다이렉트합니다.")
    @ApiResponse(responseCode = "200", description = "검증 성공")
    @GetMapping("/completeMobile")
    public ResponseEntity<Object> verifyMobilePaymentCompletion(@RequestParam("imp_uid") String impUid,
            @RequestParam("merchant_uid") String merchantUid,
            @RequestParam(value = "error_code", required = false) String errorCode,
            @RequestParam(value = "error_msg", required = false) String errorMsg,
            UriComponentsBuilder uriComponentsBuilder) throws JsonProcessingException {

        URI redirectUri = paymentService.verifyMobilePayment(impUid, merchantUid, errorCode, errorMsg);
        log.info("redirectUri: {}", redirectUri);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

    @Operation(summary = "결제 사전 검증 API", description = "결제 사전 검증 API 입니다.")
    @PostMapping("/prepare")
    public ResponseEntity<?> verifyPaymentCreation(@RequestBody PaymentPrepareRequest paymentPrepareRequest) {
        return ResponseEntity.ok(new ServerResponse<>(paymentService.preparePayment(paymentPrepareRequest)));
    }

    @Operation(summary = "포트원 웹훅 API", description = "포트원 웹훅 처리 API 입니다.")
    @PostMapping("/portone-webhook")
    public ResponseEntity<?> portoneWebhook(@RequestBody PortOneWebHookRequest webHookRequest) {
        return ResponseEntity.ok(new ServerResponse<>(paymentService.webhook(webHookRequest)));
    }
}
