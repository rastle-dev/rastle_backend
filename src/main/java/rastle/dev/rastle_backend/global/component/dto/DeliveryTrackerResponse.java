package rastle.dev.rastle_backend.global.component.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import rastle.dev.rastle_backend.global.common.enums.DeliveryTrackerStatus;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class DeliveryTrackerResponse {
    @Getter
    private final Map<String, Object> map;
    private final ObjectMapper objectMapper;

    public DeliveryTrackerStatus getLastEventStatus() {
        Map<String, Object> trackMap = (Map<String, Object>) map.get("data");
        Map<String, Object> eventMap = (Map<String, Object>) trackMap.get("track");
        Map<String, Object> lastEventMap = (Map<String, Object>) eventMap.get("lastEvent");
        Map<String, Object> statusMap = (Map<String, Object>) lastEventMap.get("status");

        return DeliveryTrackerStatus.getFromStatus((String) statusMap.get("code"));
    }

}
