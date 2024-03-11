package rastle.dev.rastle_backend.global.common.enums;

import lombok.Getter;

@Getter
public enum DataSourceType {
    MASTER("master"), SLAVE("slave");

    private final String name;

    DataSourceType(String name) {
        this.name = name;
    }
}
