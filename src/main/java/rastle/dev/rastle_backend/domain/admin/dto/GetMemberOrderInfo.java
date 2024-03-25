package rastle.dev.rastle_backend.domain.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetMemberOrderInfo {
    OrderStatus orderStatus;
    LocalDateTime paidAt;
    Long orderNumber;
    Long productOrderNumber;
    String productName;
    String option;
    Long count;
    String receiverName;
    String receiverTel;
    String receiverEmail;
    String receiverAddress;
    String receiverPostCode;
    String deliveryService;
    String deliveryMsg;
    String trackingNumber;
    String impId;
    Long productPrice;
    Long productOrderTotalPrice;
    Long orderPrice;
    Long paymentPrice;
    Long deliveryPrice;
    Long additionalDeliveryPrice;
    String paymentMethod;
    Long couponAmount;
    @QueryProjection
    public GetMemberOrderInfo(OrderStatus orderStatus, LocalDateTime paidAt, Long orderNumber, Long productOrderNumber, String productName, String color, String size, Long count, String receiverName, String receiverTel, String receiverEmail, String receiverAddress, String receiverPostCode, String deliveryService, String deliveryMsg, String trackingNumber, String impId, Long productPrice, Long productOrderTotalPrice, Long orderPrice, Long paymentPrice, Long deliveryPrice, Long additionalDeliveryPrice, String paymentMethod, Long couponAmount) {
        this.orderStatus = orderStatus;
        this.paidAt = paidAt;
        this.orderNumber = orderNumber;
        this.productOrderNumber = productOrderNumber;
        this.productName = productName;
        this.option = colorSizeToOption(color, size);
        this.count = count;
        this.receiverName = receiverName;
        this.receiverTel = receiverTel;
        this.receiverEmail = receiverEmail;
        this.receiverAddress = receiverAddress;
        this.receiverPostCode = receiverPostCode;
        this.deliveryService = deliveryService;
        this.deliveryMsg = deliveryMsg;
        this.trackingNumber = trackingNumber;
        this.impId = impId;
        this.productPrice = productPrice;
        this.productOrderTotalPrice = productOrderTotalPrice;
        this.orderPrice = orderPrice;
        this.paymentPrice = paymentPrice;
        this.deliveryPrice = deliveryPrice;
        this.additionalDeliveryPrice = additionalDeliveryPrice;
        this.paymentMethod = paymentMethod;
        this.couponAmount = couponAmount;
    }

    private String colorSizeToOption(String color, String size) {
        return "색상 : " + color + " 사이즈 : " + size;
    }
}
