package rastle.dev.rastle_backend.domain.Event.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.Event.dto.EventDTO;
import rastle.dev.rastle_backend.domain.Event.dto.EventDTO.EventCreateRequest;
import rastle.dev.rastle_backend.domain.Event.model.Event;
import rastle.dev.rastle_backend.domain.Event.repository.EventRepository;
import rastle.dev.rastle_backend.global.util.TimeUtil;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;


    @Transactional
    public String createEvent(EventCreateRequest createRequest) {
        Event newEvent = Event.builder()
                .name(createRequest.getName())
                .eventStartDate(TimeUtil.convertStringToLocalDateTime(createRequest.getStartDate(),0,0))
                .eventEndDate(TimeUtil.convertStringToLocalDateTime(createRequest.getEndDate(), 23, 59))
                .build();
        eventRepository.save(newEvent);
        return "CREATED";
    }
}
