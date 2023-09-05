package rastle.dev.rastle_backend.domain.Category.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CategoryInfo {
    Long id;
    String name;
    String imageUrls;

}
