package rastle.dev.rastle_backend.domain.Event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rastle.dev.rastle_backend.domain.Event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.Event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("select new rastle.dev.rastle_backend.domain.Event.dto.EventInfo(" +
            "e.id, " +
            "e.name, " +
            "e.eventStartDate, " +
            "e.eventEndDate, " +
            "e.imageUrls," +
            "e.description) from Event e")
    Page<EventInfo> getEventInfo(Pageable pageable);
}
