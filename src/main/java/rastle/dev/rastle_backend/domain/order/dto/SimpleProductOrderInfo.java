package rastle.dev.rastle_backend.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository.SimpleProductOrderInterface;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimpleProductOrderInfo {
    @Schema(description = "상품 썸네일", defaultValue = "https://~~.png")
    String thumbnailUrl;
    @Schema(description = "상품 아이디", defaultValue = "1")
    Long productId;
    @Schema(description = "상품 주문 번호", defaultValue = "11234~~")
    Long productOrderNumber;
    @Schema(description = "상품 이름", defaultValue = "연그레이 와이드 팬츠")
    String name;
    @Schema(description = "상품 색상", defaultValue = "BLUE")
    String color;
    @Schema(description = "상품 사이즈", defaultValue = "M")
    String size;
    @Schema(description = "상품 구매 수량", defaultValue = "1")
    Long count;
    @Schema(description = "상품 1개 금액", defaultValue = "30000")
    Long price;
    @Schema(description = "상품 구매 총 금액", defaultValue = "30000")
    Long totalPrice;
    @Schema(description = "상품 상태", defaultValue = "CREATED")
    OrderStatus status;
    @Schema(description = "취소 수량", defaultValue = "1")
    Long cancelAmount;
    @Schema(description = "취소 요청 수량", defaultValue = "0")
    Long cancelRequestAmount;
    @Schema(description = "송장 번호", defaultValue = "23404321~")
    String trackingNumber;

    public static SimpleProductOrderInfo fromInterface(SimpleProductOrderInterface simpleProductOrderInterface) {
        return new SimpleProductOrderInfo(
            simpleProductOrderInterface.getThumbnailUrl(),
            simpleProductOrderInterface.getProductId(),
            simpleProductOrderInterface.getProductOrderNumber(),
            simpleProductOrderInterface.getName(),
            simpleProductOrderInterface.getColor(),
            simpleProductOrderInterface.getSize(),
            simpleProductOrderInterface.getCount(),
            simpleProductOrderInterface.getPrice(),
            simpleProductOrderInterface.getTotalPrice(),
            OrderStatus.getFromStatus(simpleProductOrderInterface.getStatus()),
            simpleProductOrderInterface.getCancelAmount(),
            simpleProductOrderInterface.getCancelRequestAmount(),
            simpleProductOrderInterface.getTrackingNumber()
        );
    }
}
