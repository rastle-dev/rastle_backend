package rastle.dev.rastle_backend.domain.Market.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class MarketInfo {
    Long id;
    String name;
    LocalDateTime startDate;
}
