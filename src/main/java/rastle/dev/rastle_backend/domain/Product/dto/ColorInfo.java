package rastle.dev.rastle_backend.domain.Product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ColorInfo {
    String color;
    String size;
    int count;
}
