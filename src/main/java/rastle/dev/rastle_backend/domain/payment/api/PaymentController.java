package rastle.dev.rastle_backend.domain.payment.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "결제 API", description = "결제 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
}
