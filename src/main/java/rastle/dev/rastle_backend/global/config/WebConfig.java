package rastle.dev.rastle_backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000", "https://localhost:3000", "https://www.recordyslow.com", "https://recordyslow.com")
                .allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD", "OPTIONS", "PUT")
                .allowCredentials(true)
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .maxAge(3000);
    }

}
