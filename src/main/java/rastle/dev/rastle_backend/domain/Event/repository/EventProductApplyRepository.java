package rastle.dev.rastle_backend.domain.Event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Event.model.EventProductApply;

public interface EventProductApplyRepository extends JpaRepository<EventProductApply, Long> {
}
