package rastle.dev.rastle_backend.global.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static rastle.dev.rastle_backend.global.common.constants.TimeConstants.ASIA_SEOUL;

@Component
@Slf4j
public class OrderNumberComponent {
    public Long createOrderNumber(Long orderId) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(ASIA_SEOUL));
        String orderNumber = now.toEpochSecond(ZoneOffset.UTC) + convertIdToString(orderId);
        return Long.parseLong(orderNumber);
    }

    public Long createProductOrderNumber(Long orderId, Long orderProductId) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(ASIA_SEOUL));
        String orderNumber = now.toEpochSecond(ZoneOffset.UTC) + convertIdToString(orderId) + convertIdToString(orderProductId);
        return Long.parseLong(orderNumber);
    }

    private String convertIdToString(Long id) {
        int length = Long.toString(id).length();
        StringBuilder sb = new StringBuilder();
        while (length < 5) {
            sb.append(0);
            length += 1;
        }
        sb.append(id);
        return sb.toString();
    }

}
