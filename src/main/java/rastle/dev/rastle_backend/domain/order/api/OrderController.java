package rastle.dev.rastle_backend.domain.order.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.order.application.OrderService;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.MemberOrderInfo;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.OrderCreateRequest;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.OrderCreateResponse;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.OrderDetailResponse;
import rastle.dev.rastle_backend.domain.order.dto.request.OrderCancelRequest;
import rastle.dev.rastle_backend.domain.order.dto.response.OrderCancelResponse;
import rastle.dev.rastle_backend.global.response.ServerResponse;
import rastle.dev.rastle_backend.global.util.SecurityUtil;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@Tag(name = "주문 관련 API", description = "주문 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "주문 등록 API", description = "주문하기 버튼을 눌러서 주문을 생성하는 API입니다")
    @ApiResponse(responseCode = "200", description = "주문 생성 성공시", content = @Content(schema = @Schema(implementation = OrderCreateResponse.class)))
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@RequestBody OrderCreateRequest orderCreateRequest) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(new ServerResponse<>(orderService.createOrderDetail(memberId, orderCreateRequest)));
    }

    @Operation(summary = "주문 상세 조회 API", description = "주문 상세 조회 API")
    @GetMapping("/{orderId}")
    public ResponseEntity<ServerResponse<OrderDetailResponse>> getOrderDetail(@PathVariable("orderId") Long orderId) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(new ServerResponse<>(orderService.getOrderDetail(memberId, orderId)));
    }


    @Operation(summary = "주문 리스트 조회 API", description = "멤버 주문 리스트 조회 API 입니다")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = MemberOrderInfo.class)))
    @GetMapping("")
    public ResponseEntity<?> getMemberOrders(
        @Parameter(name = "page", description = "페이지 번호", in = QUERY, required = false)
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @Parameter(name = "size", description = "페이지 크기", in = QUERY, required = false)
        @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(new ServerResponse<>(orderService.getMemberOrder(memberId, PageRequest.of(page, size))));
    }

    @Operation(summary = "주문 취소 API", description = "주문 취소 API입니다.")
    @ApiResponse(responseCode = "200", description = "주문 취소 성공시", content = @Content(schema = @Schema(implementation = OrderCancelResponse.class)))
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(
        @RequestBody OrderCancelRequest orderCancelRequest
        ) {
        Long memberId = SecurityUtil.getCurrentMemberId();
        return ResponseEntity.ok(new ServerResponse<>(orderService.cancelOrder(memberId, orderCancelRequest)));

    }

}
