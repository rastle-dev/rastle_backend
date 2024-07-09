package rastle.dev.rastle_backend.domain.hibernate.repository.mysql;

import java.util.List;

import org.hibernate.annotations.Cache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import jakarta.persistence.QueryHint;
import rastle.dev.rastle_backend.domain.hibernate.dto.MemberInfo;
import rastle.dev.rastle_backend.domain.hibernate.model.TestMember;

public interface TestMemberRepository extends JpaRepository<TestMember, Long>{

    @QueryHints(value = { @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @Query("SELECT new rastle.dev.rastle_backend.domain.hibernate.dto.MemberInfo(m.id, m.name, m.email) FROM TestMember m")
    List<MemberInfo> getAllMemberInfos();

}
