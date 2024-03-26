package rastle.dev.rastle_backend.global.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    CREATED("CREATED", 0), PENDING("PENDING", 1), PAID("PAID", 2), CANCELLED("CANCELLED", 3), DELIVERY_STARTED("DELIVERY_STARTED",3), DELIVERED("DELIVERED", 4), COMPLETED("COMPLETED", 5), RETURNED("RETURNED", 6), FAILED("FAILED", 7);
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
