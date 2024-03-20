package rastle.dev.rastle_backend.global.common.enums;

import lombok.Getter;

@Getter
public enum ProductSort {
    POPULAR("popular"),
    RECENT("recent"),
    SOLD("sold");

    private final String id;

    ProductSort(String id) {
        this.id = id;
    }

    public static ProductSort getById(String id) {
        for (ProductSort productSort : ProductSort.values()) {
            if (productSort.id.equals(id)) {
                return productSort;
            }
        }
        return RECENT;
    }
}
