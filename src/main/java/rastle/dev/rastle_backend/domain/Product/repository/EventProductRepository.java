package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.model.EventProduct;

public interface EventProductRepository extends JpaRepository<EventProduct, Long> {
    @Query(
            "select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
                    "ep.id, " +
                    "ep.name, " +
                    "ep.price, " +
                    "ep.mainThumbnailImage, " +
                    "ep.subThumbnailImage, " +
                    "ep.isEventProduct, " +
                    "ep.discount, " +
                    "ep.displayOrder, " +
                    "ep.visible) " +
                    "from EventProduct ep"
    )
    Page<SimpleProductInfo> getEventProducts(Pageable pageable);
}
