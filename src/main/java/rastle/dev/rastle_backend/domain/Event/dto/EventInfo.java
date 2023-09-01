package rastle.dev.rastle_backend.domain.Event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventInfo {
    Long id;
    String name;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
