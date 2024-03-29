package rastle.dev.rastle_backend.global.common.enums;

import lombok.Getter;

@Getter
public enum DeliveryTrackerStatus {
    UNKNOWN("UNKNOWN"),
    INFORMATION_RECEIVED("INFORMATION_RECEIVED"),
    AT_PICKUP("AT_PICKUP"),
    IN_TRANSIT("IN_TRANSIT"),
    OUT_FOR_DELIVERY("OUT_FOR_DELIVERY"),
    ATTEMPT_FAIL("ATTEMPT_FAIL"),
    DELIVERED("DELIVERED"),
    AVAILABLE_FOR_PICKUP("AVAILABLE_FOR_PICKUP"),
    EXCEPTION("EXCEPTION"),
    ;
    private final String status;

    DeliveryTrackerStatus(String status) {
        this.status = status;
    }

    public static DeliveryTrackerStatus getFromStatus(String status) {
        for (DeliveryTrackerStatus  deliveryTrackerStatus : DeliveryTrackerStatus.values()) {
            if (deliveryTrackerStatus.status.equals(status)) {
                return deliveryTrackerStatus;
            }
        }
        return null;
    }
}
