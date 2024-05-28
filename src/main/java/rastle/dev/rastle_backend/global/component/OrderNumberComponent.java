package rastle.dev.rastle_backend.global.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

import static rastle.dev.rastle_backend.global.common.constants.TimeConstants.ASIA_SEOUL;

@Component
@Slf4j
public class OrderNumberComponent {
    public Long createOrderNumber(Long orderId) {

        String orderNumber = dateToString() + convertIdToString(orderId);
        return Long.parseLong(orderNumber);
    }

    private String dateToString() {
        LocalDate nowDate = LocalDate.now(ZoneId.of(ASIA_SEOUL));
        String month = Integer.toString(nowDate.getMonth().getValue());
        if (month.length() == 1) {
            month = "0" + month;
        }
        String date = Integer.toString(nowDate.getDayOfMonth());
        if (date.length() == 1) {
            date = "0" + date;
        }
        return nowDate.getYear() + month + date;
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
