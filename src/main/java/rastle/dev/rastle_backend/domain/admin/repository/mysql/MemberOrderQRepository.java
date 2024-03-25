package rastle.dev.rastle_backend.domain.admin.repository.mysql;

import org.springframework.data.domain.Page;
import rastle.dev.rastle_backend.domain.admin.dto.GetMemberOrderCondition;
import rastle.dev.rastle_backend.domain.admin.dto.GetMemberOrderInfo;

public interface MemberOrderQRepository {
    Page<GetMemberOrderInfo> getMemberOrderInfo(GetMemberOrderCondition condition);
}
