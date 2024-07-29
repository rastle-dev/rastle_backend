package rastle.dev.rastle_backend.domain.event.repository.mysql;

import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.event.model.Event;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<Event> findById(Long id);
    @Query("select new rastle.dev.rastle_backend.domain.event.dto.EventInfo(" +
        "e.id, " +
        "e.name, " +
        "e.eventStartDate, " +
        "e.eventEndDate, " +
        "e.imageUrls," +
        "e.description, " +
        "e.visible) from Event e")
    Page<EventInfo> getEventInfo(Pageable pageable);

    @Query("select new rastle.dev.rastle_backend.domain.event.dto.EventInfo(" +
        "e.id, " +
        "e.name, " +
        "e.eventStartDate, " +
        "e.eventEndDate, " +
        "e.imageUrls," +
        "e.description, " +
        "e.visible) from Event e WHERE e.visible = :visible")
    Page<EventInfo> getEventInfoByVisibility(@Param("visible") boolean visible, Pageable pageable);
}
