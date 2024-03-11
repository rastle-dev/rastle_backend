package rastle.dev.rastle_backend.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties("spring.datasource")
public class ReplicationDataSourceProperty {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private final Map<String, Slave> slaves = new HashMap<>();

    @Getter
    @Setter
    public static class Slave {
        private String name;
        private String driverClassName;
        private String username;
        private String password;
        private String url;
    }
}
