package rastle.dev.rastle_backend.domain.product.dto;

import lombok.*;
import rastle.dev.rastle_backend.global.common.enums.VisibleStatus;
import rastle.dev.rastle_backend.global.util.CustomPageRequest;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProductRequest {
    CustomPageRequest customPageRequest;
    VisibleStatus visibleStatus;
    Long bundleId;
    Long eventId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetProductRequest that = (GetProductRequest) o;
        return Objects.equals(customPageRequest, that.customPageRequest) && visibleStatus == that.visibleStatus && Objects.equals(bundleId, that.bundleId) && Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customPageRequest, visibleStatus, bundleId, eventId);
    }

    @Override
    public String toString() {
        return "GetProductRequest{" +
            "pageable=" + customPageRequest +
            ", visibleStatus=" + visibleStatus +
            ", bundleId=" + bundleId +
            ", eventId=" + eventId +
            '}';
    }
}
