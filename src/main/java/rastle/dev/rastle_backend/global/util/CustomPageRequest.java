package rastle.dev.rastle_backend.global.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomPageRequest implements  Serializable {

    private int page;
    private int size;
    private List<CustomSortOrder> orders;


    public static CustomPageRequest of(Pageable pageable) {
        return new CustomPageRequest(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            pageable.getSort().stream().map(CustomSortOrder::fromOrder).toList()
        );
    }

    public static Pageable toPageable(CustomPageRequest customPageRequest) {
        return PageRequest.of(customPageRequest.getPage(), customPageRequest.getSize(), Sort.by(customPageRequest.getOrders().stream().map(CustomSortOrder::toOrder).toList()));
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof CustomPageRequest that)) {
            return false;
        } else {
            return super.equals(that) && this.orders.equals(that.orders);
        }
    }

    public int hashCode() {
        return 31 * super.hashCode() + this.orders.hashCode();
    }

    public String toString() {
        return String.format("Page request [number: %d, size %d, sort: %s]", this.page, this.size, this.orders);
    }
}
