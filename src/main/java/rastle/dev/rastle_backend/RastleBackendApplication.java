package rastle.dev.rastle_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConfigurationPropertiesScan
@EnableScheduling
@EnableCaching
@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RastleBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RastleBackendApplication.class, args);
    }

}
