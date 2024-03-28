package rastle.dev.rastle_backend.global.common.enums;

import lombok.Getter;

@Getter
public enum CancelRequestStatus {
    PENDING("PENDING"), DENIED("DENIED"), COMPLETED("COMPLETED");

    private final String index;

    CancelRequestStatus(String index) {
        this.index = index;
    }

    public static CancelRequestStatus getFromIndex(String index) {
        for (CancelRequestStatus status : CancelRequestStatus.values()) {
            if (status.getIndex().equals(index)) {
                return status;
            }
        }
        return null;
    }


}
