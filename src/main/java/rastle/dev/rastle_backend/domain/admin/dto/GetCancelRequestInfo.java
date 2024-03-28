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
    Long orderNumber;
    Long productOrderNumber;
    OrderStatus orderStatus;
    Long cancelPrice;
    String productName;
    String productColor;
    String productSize;
    Long productCount;
    String reason;
    String userName;
    String userTel;
    String userEmail;
    String userAddress;
    String userPostCode;

    @QueryProjection
    public GetCancelRequestInfo(String impId, Long orderNumber, Long productOrderNumber, OrderStatus orderStatus, Long cancelPrice, String productName, String productColor, String productSize, Long productCount, String reason, String userName, String userTel, String userEmail, String userAddress, String userPostCode) {
        this.impId = impId;
        this.orderNumber = orderNumber;
        this.productOrderNumber = productOrderNumber;
        this.orderStatus = orderStatus;
        this.cancelPrice = cancelPrice;
        this.productName = productName;
        this.productColor = productColor;
        this.productSize = productSize;
        this.productCount = productCount;
        this.reason = reason;
        this.userName = userName;
        this.userTel = userTel;
        this.userEmail = userEmail;
        this.userAddress = userAddress;
        this.userPostCode = userPostCode;
    }
}
