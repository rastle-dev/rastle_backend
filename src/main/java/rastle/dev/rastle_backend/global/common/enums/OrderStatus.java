package rastle.dev.rastle_backend.global.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("CREATED"), PENDING("PENDING"), PAID("PAID"), DELIVERY_STARTED("DELIVERY_STARTED"), DELIVERED("DELIVERED"), COMPLETED("COMPLETED"), CANCELLED("CANCELLED"), FAILED("FAILED");
    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public static OrderStatus getFromStatus(String status) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getStatus().equals(status)) {
                return orderStatus;
            }
        }
        return null;
    }
}
