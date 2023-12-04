package rastle.dev.rastle_backend.domain.order.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "주문 관련 API", description = "주문 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
}
