package rastle.dev.rastle_backend.domain.Event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
