package rastle.dev.rastle_backend.domain.Event.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.Event.dto.EventDTO;
import rastle.dev.rastle_backend.domain.Event.dto.EventDTO.EventCreateRequest;
import rastle.dev.rastle_backend.domain.Event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.Event.model.Event;
import rastle.dev.rastle_backend.domain.Event.repository.EventRepository;
import rastle.dev.rastle_backend.global.util.TimeUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;




    @Transactional(readOnly = true)
    public Page<EventInfo> getEventInfo(Pageable pageable) {
        return eventRepository.getEventInfo(pageable);
    }
}
