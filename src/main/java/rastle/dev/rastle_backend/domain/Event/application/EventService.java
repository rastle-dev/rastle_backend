package rastle.dev.rastle_backend.domain.Event.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.Event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.Event.repository.EventRepository;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.repository.EventProductRepository;

import java.util.List;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstant.ALL;
import static rastle.dev.rastle_backend.global.common.constants.CommonConstant.TRUE;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventProductRepository eventProductRepository;

    @Transactional(readOnly = true)
    public Page<EventInfo> getEventInfo(String visible, Pageable pageable) {
        if (visible.equals(ALL)) {
            return eventRepository.getEventInfo(pageable);
        } else if (visible.equals(TRUE)) {
            return eventRepository.getEventInfoByVisibility(true, pageable);
        } else {
            return eventRepository.getEventInfoByVisibility(false, pageable);
        }
    }

    @Transactional(readOnly = true)
    public List<SimpleProductInfo> getEventProducts(Long id) {
        return eventProductRepository.getEventProductInfosByEventId(id);
    }
}
