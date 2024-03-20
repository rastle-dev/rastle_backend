package rastle.dev.rastle_backend.global.common.enums;

import lombok.Getter;

@Getter
public enum VisibleStatus {
    ALL("ALL"), FALSE("FALSE"), TRUE("TRUE");

    private final String id;

    VisibleStatus(String id) {
        this.id = id;
    }

    public static VisibleStatus getById(String id) {
        for (VisibleStatus visibleStatus : VisibleStatus.values()) {
            if (visibleStatus.getId().equals(id)) {
                return visibleStatus;
            }
        }
        return ALL;
    }
}
