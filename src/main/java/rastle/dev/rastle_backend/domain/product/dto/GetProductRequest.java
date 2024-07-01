package rastle.dev.rastle_backend.domain.product.dto;

import lombok.*;
import org.springframework.data.domain.Pageable;
import rastle.dev.rastle_backend.global.common.enums.VisibleStatus;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProductRequest {
    Pageable pageable;
    VisibleStatus visibleStatus;
    Long bundleId;
    Long eventId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetProductRequest that = (GetProductRequest) o;
        return Objects.equals(pageable, that.pageable) && visibleStatus == that.visibleStatus && Objects.equals(bundleId, that.bundleId) && Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageable, visibleStatus, bundleId, eventId);
    }

    @Override
    public String toString() {
        return "GetProductRequest{" +
            "pageable=" + pageable +
            ", visibleStatus=" + visibleStatus +
            ", bundleId=" + bundleId +
            ", eventId=" + eventId +
            '}';
    }
}
