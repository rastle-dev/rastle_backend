package rastle.dev.rastle_backend.domain.event.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.event.model.EventProductApply;

public interface EventProductApplyRepository extends JpaRepository<EventProductApply, Long> {
}
