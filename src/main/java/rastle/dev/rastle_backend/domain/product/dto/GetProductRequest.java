package rastle.dev.rastle_backend.domain.product.dto;

import lombok.*;
import org.springframework.data.domain.Pageable;
import rastle.dev.rastle_backend.global.common.enums.VisibleStatus;

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
}
