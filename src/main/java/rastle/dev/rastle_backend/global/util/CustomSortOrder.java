package rastle.dev.rastle_backend.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomSortOrder {
    // "direction":"ASC","property":"displayOrder","ignoreCase":false,"nullHandling":"NATIVE","descending":false,"ascending":true
    private String direction;
    private String property;
    private boolean ignoreCase;
    private String nullHandling;
    private boolean ascending;
    private boolean descending;


    public static CustomSortOrder fromOrder(Sort.Order order) {
        return new CustomSortOrder(order.getDirection().toString(), order.getProperty(), order.isIgnoreCase(), order.getNullHandling().toString(), order.isAscending(), order.isDescending());
    }

    public static Sort.Order toOrder(CustomSortOrder sortOrder) {
        if (sortOrder.ascending) {
            return Sort.Order.asc(sortOrder.getProperty());
        } else {
            return Sort.Order.desc(sortOrder.getProperty());
        }
    }
}
