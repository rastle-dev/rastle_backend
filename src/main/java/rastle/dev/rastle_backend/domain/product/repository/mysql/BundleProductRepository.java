package rastle.dev.rastle_backend.domain.product.repository.mysql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.product.dto.BundleProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

import java.util.List;
import java.util.Optional;

public interface BundleProductRepository extends JpaRepository<ProductBase, Long> {

    Boolean existsBundleProductByBundleId(Long id);

    @Query("select new rastle.dev.rastle_backend.domain.product.dto.BundleProductInfo(" +
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
        "from ProductBase bp " +
        "join Bundle b on b.id = bp.bundle.id WHERE :lowerBound <= b.id AND b.id <= :upperBound ORDER BY bp.displayOrder DESC")
    List<BundleProductInfo> getBundleProducts(@Param("lowerBound") Long lowerBound,
                                              @Param("upperBound") Long upperBound);

    @Query("select new rastle.dev.rastle_backend.domain.product.dto.BundleProductInfo(" +
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
        "from ProductBase bp " +
        "join Bundle b on b.id = bp.bundle.id WHERE b.visible = :visible and :lowerBound <= b.id AND b.id <= :upperBound ORDER BY bp.displayOrder DESC")
    List<BundleProductInfo> getBundleProductsByVisibility(@Param("visible") boolean visible,
                                                          @Param("lowerBound") Long lowerBound, @Param("upperBound") Long upperBound);

    @Query("select new rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo(" +
        "bp.id, " +
        "bp.name, " +
        "bp.price, " +
        "bp.mainThumbnailImage, " +
        "bp.subThumbnailImage," +
        "bp.discountPrice, " +
        "bp.displayOrder, " +
        "bp.visible, " +
        "bp.category.id, " +
        "bp.bundle.id, " +
        "bp.event.id, " +
        "bp.eventApplyCount, " +
        "bp.soldCount) " +
        "from ProductBase bp " +
        "WHERE bp.bundle.id = :id ORDER BY bp.displayOrder DESC")
    List<SimpleProductInfo> getBundleProductInfosByBundleId(@Param("id") Long id);

    @Query("select new rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo(" +
        "bp.id, " +
        "bp.name, " +
        "bp.price, " +
        "bp.mainThumbnailImage, " +
        "bp.subThumbnailImage," +
        "bp.discountPrice, " +
        "bp.displayOrder, " +
        "bp.visible, " +
        "bp.category.id, " +
        "bp.bundle.id, " +
        "bp.event.id, " +
        "bp.eventApplyCount, " +
        "bp.soldCount) " +
        "from ProductBase bp " +
        "WHERE bp.id = :id ")
    Optional<SimpleProductInfo> getBundleProductInfoById(@Param("id") Long id);

}
