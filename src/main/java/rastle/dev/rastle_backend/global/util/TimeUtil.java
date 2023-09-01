package rastle.dev.rastle_backend.global.util;

import java.time.LocalDateTime;

public class TimeUtil {
    public static LocalDateTime convertStringToLocalDateTime(String time, String hour, String minute,String second) {
        String[] parsed = time.split("-");
        return LocalDateTime.of(Integer.parseInt(parsed[0]), Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]), Integer.parseInt(hour), Integer.parseInt(minute), Integer.parseInt(second));
    }
}
