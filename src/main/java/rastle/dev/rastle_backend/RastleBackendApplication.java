package rastle.dev.rastle_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.test.context.ActiveProfiles;

@ConfigurationPropertiesScan
@EnableScheduling
// @SpringBootApplication
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class RastleBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RastleBackendApplication.class, args);
    }

}
