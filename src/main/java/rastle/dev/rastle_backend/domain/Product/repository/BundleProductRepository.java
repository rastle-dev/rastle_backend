package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.model.BundleProduct;

import java.time.LocalDateTime;

public interface BundleProductRepository extends JpaRepository<BundleProduct, Long> {
    @Query(
            "select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
                    "bp.id, " +
                    "bp.name, " +
                    "bp.price, " +
                    "bp.mainThumbnailImage, " +
                    "bp.subThumbnailImage, " +
                    "bp.isEventProduct, " +
                    "bp.discount, " +
                    "bp.displayOrder, " +
                    "bp.visible) " +
                    "from BundleProduct bp " +
                    "join Bundle b on b.id = bp.bundle.id where  b.saleStartTime >= :current"
    )
    Page<SimpleProductInfo> getBundleProducts(@Param("current") LocalDateTime current, Pageable pageable);
}
