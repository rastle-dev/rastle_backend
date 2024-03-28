package rastle.dev.rastle_backend.domain.admin.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;

@Getter
@Setter
@NoArgsConstructor
public class GetCancelRequestInfo {
    String impId;
    Long cancelRequestId;
    Long orderNumber;
    Long productOrderNumber;
    OrderStatus orderStatus;
    Long price;
    Long productOrderTotalPrice;
    String productName;
    String productColor;
    String productSize;
    Long productCount;
    String reason;
    Long cancelAmount;
    String userName;
    String userTel;
    String userEmail;
    String userAddress;
    String userPostCode;

    @QueryProjection
    public GetCancelRequestInfo(String impId, Long cancelRequestId, Long orderNumber, Long productOrderNumber, OrderStatus orderStatus, Long price, Long productOrderTotalPrice, String productName, String productColor, String productSize, Long productCount, String reason, Long cancelAmount, String userName, String userTel, String userEmail, String userAddress, String userPostCode) {
        this.impId = impId;
        this.cancelRequestId = cancelRequestId;
        this.orderNumber = orderNumber;
        this.productOrderNumber = productOrderNumber;
        this.orderStatus = orderStatus;
        this.price = price;
        this.productOrderTotalPrice = productOrderTotalPrice;
        this.productName = productName;
        this.productColor = productColor;
        this.productSize = productSize;
        this.productCount = productCount;
        this.reason = reason;
        this.cancelAmount = cancelAmount;
        this.userName = userName;
        this.userTel = userTel;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.userPostCode = userPostCode;
    }
}
