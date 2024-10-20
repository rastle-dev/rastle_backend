package rastle.dev.rastle_backend.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("http://localhost:3000", "https://localhost:3000", "https://www.rastle.site",
                "https://rastle.site", "http://localhost:8080")
            .allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD", "OPTIONS", "PUT")
            .allowCredentials(true)
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            // .exposedHeaders("Set-Cookie")
            .maxAge(3000);
    }

}
