package rastle.dev.rastle_backend.global.config;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class GeoIpConfig {
    @Bean
    public DatabaseReader databaseReader() throws IOException, GeoIp2Exception {
        ClassPathResource resource = new ClassPathResource("GeoLite2-Country.mmdb");
        return new DatabaseReader.Builder(resource.getInputStream()).build();
    }
}
