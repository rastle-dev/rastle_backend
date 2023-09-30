package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.dto.BundleProductInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.model.BundleProduct;

import java.time.LocalDateTime;
import java.util.List;

public interface BundleProductRepository extends JpaRepository<BundleProduct, Long> {

    Boolean existsBundleProductByBundleId(Long id);

    @Query(
            "select new rastle.dev.rastle_backend.domain.Product.dto.BundleProductInfo(" +
                    "b.id, " +
                    "b.name, " +
                    "b.imageUrls, " +
                    "b.description, " +
                    "b.saleStartTime, " +
                    "b.visible, " +
                    "bp.id, " +
                    "bp.name, " +
                    "bp.price, " +
                    "bp.mainThumbnailImage, " +
                    "bp.subThumbnailImage, " +
                    "bp.discount, " +
                    "bp.displayOrder, " +
                    "bp.visible) " +
                    "from BundleProduct bp " +
                    "join Bundle b on b.id = bp.bundle.id WHERE :lowerBound <= b.id AND b.id <= :upperBound"
    )
    List<BundleProductInfo> getBundleProducts(@Param("lowerBound") Long lowerBound, @Param("upperBound") Long upperBound);
    @Query(
            "select new rastle.dev.rastle_backend.domain.Product.dto.BundleProductInfo(" +
                    "b.id, " +
                    "b.name, " +
                    "b.imageUrls, " +
                    "b.description, " +
                    "b.saleStartTime, " +
                    "b.visible, " +
                    "bp.id, " +
                    "bp.name, " +
                    "bp.price, " +
                    "bp.mainThumbnailImage, " +
                    "bp.subThumbnailImage, " +
                    "bp.discount, " +
                    "bp.displayOrder, " +
                    "bp.visible) " +
                    "from BundleProduct bp " +
                    "join Bundle b on b.id = bp.bundle.id WHERE b.visible = :visible and :lowerBound <= b.id AND b.id <= :upperBound"
    )
    List<BundleProductInfo> getBundleProductsByVisibility(@Param("visible") boolean visible, @Param("lowerBound") Long lowerBound, @Param("upperBound") Long upperBound);

}
