package rastle.dev.rastle_backend.domain.admin.repository.mysql;

import org.springframework.data.domain.Page;
import rastle.dev.rastle_backend.domain.admin.dto.GetCancelRequestCondition;
import rastle.dev.rastle_backend.domain.admin.dto.GetCancelRequestInfo;

public interface CancelRequestQRepository {
    Page<GetCancelRequestInfo> getCancelRequestInfo(GetCancelRequestCondition getCancelRequestCondition);
}
