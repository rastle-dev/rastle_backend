package rastle.dev.rastle_backend.domain.Market.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MarketDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MarketCreateRequest {
        String name;
        String startDate;
        String endDate;
        String startHour;
        String startMinute;
        String startSecond;
        String endHour;
        String endMinute;
        String endSecond;
    }
}
