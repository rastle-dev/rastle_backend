package rastle.dev.rastle_backend.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.product.dto.EventProductInfo.EventProductOutInfo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventProductQueryResult {
    List<EventProductOutInfo> results;
}
