package rastle.dev.rastle_backend.domain.Event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class EventDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EventCreateRequest {
        String name;
        String startDate;
        String endDate;
    }
}
