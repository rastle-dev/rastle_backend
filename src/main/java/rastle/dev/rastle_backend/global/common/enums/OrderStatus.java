package rastle.dev.rastle_backend.global.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("CREATED", 0),
    PENDING("PENDING", 1),
    PAID("PAID", 2),
    CANCELLED("CANCELLED", 3),
    CANCEL_REQUESTED("CANCEL_REQUESTED",13),
    PARTIALLY_CANCELLED("PARTIALLY_CANCELLED",14),
    DELIVERY_READY("DELIVERY_READY",4),
    DELIVERY_STARTED("DELIVERY_STARTED",5),
    DELIVERED("DELIVERED", 6),
    COMPLETED("COMPLETED", 7),
    RETURNED("RETURNED", 8),
    FAILED("FAILED", 9),
    DELIVERY_FAILED("DELIVERY_FAILED", 10),
    DELIVERY_EXCEPTION("DELIVERY_EXCEPTION", 11),
    DELIVERY_UNKNOWN("DELIVERY_UNKNOWN", 12);

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
