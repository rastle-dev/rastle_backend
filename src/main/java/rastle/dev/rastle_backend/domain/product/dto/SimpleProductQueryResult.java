package rastle.dev.rastle_backend.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleProductQueryResult {
    List<SimpleProductInfo> simpleProductInfos;
    Long size;

    public SimpleProductQueryResult(List<SimpleProductInfo> simpleProductInfos) {
        this.simpleProductInfos = simpleProductInfos;
    }
}
