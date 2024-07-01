package rastle.dev.rastle_backend.domain.product.repository.mysql;

import rastle.dev.rastle_backend.domain.product.dto.GetProductRequest;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductQueryResult;

public interface ProductQRepository {
    SimpleProductQueryResult getProductInfos(GetProductRequest getProductRequest);
}
