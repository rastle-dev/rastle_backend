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
    public String createOrderNumber(Long orderId) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(ASIA_SEOUL));
        String orderNumber = now.toEpochSecond(ZoneOffset.UTC) + convertIdToString(orderId);
        log.info("orderNumber " + orderNumber);
        return orderNumber;
    }

    public String createProductOrderNumber(Long orderId, Long orderProductId) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of(ASIA_SEOUL));
        String orderNumber = now.toEpochSecond(ZoneOffset.UTC) + convertIdToString(orderId) + convertIdToString(orderProductId);
        log.info("product order number "+orderNumber);
        return orderNumber;
    }

    private String convertIdToString(Long id) {
        int length = Long.toString(id).length();
        StringBuilder sb = new StringBuilder();
        while (length < 4) {
            sb.append(0);
            length += 1;
        }
        sb.append(id);
        return sb.toString();
    }
}
