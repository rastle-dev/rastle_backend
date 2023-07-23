package rastle.dev.rastle_backend.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "<Rastle>",
                description = "쇼핑몰 <Rastle> API 명세서",
                version = "1.0"
        )

)
@RequiredArgsConstructor
@Configuration
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi rastleApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .displayName("API Doc Ver 1.0")
                .group("Rastle_v1")
                .pathsToMatch(paths)
                .build();
    }

}
