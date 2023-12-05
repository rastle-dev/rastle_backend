package rastle.dev.rastle_backend.domain.order.api;

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
import rastle.dev.rastle_backend.domain.order.application.OrderService;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.OrderCreateRequest;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.OrderCreateResponse;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@Tag(name = "주문 관련 API", description = "주문 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    @Operation(summary = "주문 등록 API", description = "주문하기 버튼을 눌러서 주문을 생성하는 API입니다")
    @ApiResponse(responseCode = "200", description = "주문 생성 성공시", content = @Content(schema = @Schema(implementation = OrderCreateResponse.class)))
    @PostMapping("")
    public ResponseEntity<?> createMemberOrders(@RequestBody OrderCreateRequest orderCreateRequest) {
        return ResponseEntity.ok(new ServerResponse<>(orderService.createMemberOrders(orderCreateRequest)));
    }
}
