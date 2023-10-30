package rastle.dev.rastle_backend.domain.Product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ImageInfo {
    List<String> imageUrls;
}
