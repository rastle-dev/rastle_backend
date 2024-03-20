package rastle.dev.rastle_backend.domain.product.repository.mysql;

import org.springframework.data.domain.Page;
import rastle.dev.rastle_backend.domain.product.dto.GetProductRequest;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;

public interface ProductQRepository {
    Page<SimpleProductInfo> getProductInfos(GetProductRequest getProductRequest);
}
