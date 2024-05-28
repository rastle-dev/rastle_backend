package rastle.dev.rastle_backend.global.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("CREATED", 0),
    PENDING("PENDING", 1),
    PAID("PAID", 2),
    CANCELLED("CANCELLED", 3),
    CANCEL_REQUESTED("CANCEL_REQUESTED",12),
    PARTIALLY_CANCELLED("PARTIALLY_CANCELLED",13),
    DELIVERY_STARTED("DELIVERY_STARTED",4),
    DELIVERED("DELIVERED", 5),
    COMPLETED("COMPLETED", 6),
    RETURNED("RETURNED", 7),
    FAILED("FAILED", 8),
    DELIVERY_FAILED("DELIVERY_FAILED", 9),
    DELIVERY_EXCEPTION("DELIVERY_EXCEPTION", 10),
    DELIVERY_UNKNOWN("DELIVERY_UNKNOWN", 11);

    private final String status;
    private final int index;
    OrderStatus(String status, int index) {
        this.status = status;
        this.index = index;
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
