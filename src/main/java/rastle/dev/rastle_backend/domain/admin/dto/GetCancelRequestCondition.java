package rastle.dev.rastle_backend.domain.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import rastle.dev.rastle_backend.global.common.enums.CancelRequestStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCancelRequestCondition {
    Long orderNumber;
    String receiverName;
    CancelRequestStatus cancelRequestStatus;
    Pageable pageable;
}
