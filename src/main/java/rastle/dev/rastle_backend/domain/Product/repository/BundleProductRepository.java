package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.dto.BundleProductInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleBundleOrEventProductInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.model.BundleProduct;

import java.util.List;
import java.util.Optional;

public interface BundleProductRepository extends JpaRepository<BundleProduct, Long> {

        Boolean existsBundleProductByBundleId(Long id);

        @Query("select new rastle.dev.rastle_backend.domain.Product.dto.BundleProductInfo(" +
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
                        "bp.discountPrice, " +
                        "bp.displayOrder, " +
                        "bp.visible) " +
                        "from BundleProduct bp " +
                        "join Bundle b on b.id = bp.bundle.id WHERE :lowerBound <= b.id AND b.id <= :upperBound ORDER BY bp.displayOrder ASC")
        List<BundleProductInfo> getBundleProducts(@Param("lowerBound") Long lowerBound,
                        @Param("upperBound") Long upperBound);

        @Query("select new rastle.dev.rastle_backend.domain.Product.dto.BundleProductInfo(" +
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
                        "bp.discountPrice, " +
                        "bp.displayOrder, " +
                        "bp.visible) " +
                        "from BundleProduct bp " +
                        "join Bundle b on b.id = bp.bundle.id WHERE b.visible = :visible and :lowerBound <= b.id AND b.id <= :upperBound ORDER BY bp.displayOrder ASC")
        List<BundleProductInfo> getBundleProductsByVisibility(@Param("visible") boolean visible,
                        @Param("lowerBound") Long lowerBound, @Param("upperBound") Long upperBound);

        @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
                        "bp.id, " +
                        "bp.name, " +
                        "bp.price, " +
                        "bp.mainThumbnailImage, " +
                        "bp.subThumbnailImage," +
                        "bp.isEventProduct, " +
                        "bp.discountPrice, " +
                        "bp.displayOrder, " +
                        "bp.visible, " +
                        "bp.category.id) " +
                        "from BundleProduct bp " +
                        "WHERE bp.bundle.id = :id ORDER BY bp.displayOrder ASC")
        List<SimpleProductInfo> getBundleProductInfosByBundleId(@Param("id") Long id);

        @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleBundleOrEventProductInfo(" +
                        "bp.id, " +
                        "bp.name, " +
                        "bp.price, " +
                        "bp.mainThumbnailImage, " +
                        "bp.subThumbnailImage," +
                        "bp.isEventProduct, " +
                        "bp.discountPrice, " +
                        "bp.displayOrder, " +
                        "bp.visible, " +
                        "bp.category.id, " +
                        "bp.bundle.id) " +
                        "from BundleProduct bp " +
                        "WHERE bp.id = :id ")
        Optional<SimpleBundleOrEventProductInfo> getBundleProductInfoById(@Param("id") Long id);

}
