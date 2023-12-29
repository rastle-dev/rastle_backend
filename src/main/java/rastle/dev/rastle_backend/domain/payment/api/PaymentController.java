package rastle.dev.rastle_backend.domain.payment.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rastle.dev.rastle_backend.domain.payment.application.PaymentService;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentVerificationRequest;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentVerificationResponse;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@Tag(name = "결제 API", description = "결제 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "결제 사후 검증 및 생성 API", description = "포트원 API에서 발생한 결제 요청을 검증하고, memberOrder를 생성하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "검증 성공시", content = @Content(schema = @Schema(implementation = PaymentVerificationResponse.class)))
    @PostMapping("/complete")
    public ResponseEntity<?> verifyPaymentCompletion(@RequestBody PaymentVerificationRequest paymentVerificationRequest) {
        return ResponseEntity.ok(new ServerResponse<>(paymentService.verifyPayment(paymentVerificationRequest)));
    }

    @Operation(summary = "결제 사전 검증 API", description = "결제 사전 검증 API 입니다.")
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPaymentCreation() {
        return null;
    }
}
