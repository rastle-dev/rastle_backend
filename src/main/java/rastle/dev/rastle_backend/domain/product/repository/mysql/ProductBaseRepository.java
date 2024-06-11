package rastle.dev.rastle_backend.domain.product.repository.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.product.dto.ProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

import java.util.Optional;

public interface ProductBaseRepository extends JpaRepository<ProductBase, Long> {

    @Modifying
    @Query("UPDATE ProductBase pb SET pb.soldOut = true WHERE pb.id=:id")
    void soldOutProduct(@Param("id") Long id);

    @Modifying
    @Query("UPDATE ProductBase pb SET pb.mainThumbnailImage=:mainThumbnail WHERE pb.id=:id")
    void updateProductBaseMainThumbnail(@Param("id") Long id, @Param("mainThumbnail") String mainThumbnail);

    @Modifying
    @Query("UPDATE ProductBase pb SET pb.subThumbnailImage=:subThumbnail WHERE pb.id=:id")
    void updateProductBaseSubThumbnail(@Param("id") Long id, @Param("subThumbnail") String subThumbnail);

    @Query("select new rastle.dev.rastle_backend.domain.product.dto.ProductInfo(" +
        "pb.id, " +
        "pb.name, " +
        "pb.price, " +
        "pb.mainThumbnailImage, " +
        "pb.subThumbnailImage," +
        "pb.discountPrice, " +
        "pb.displayOrder, " +
        "pb.visible, " +
        "pb.category.id, " +
        "pb.bundle.id, " +
        "pb.event.id, " +
        "pd.productMainImages, " +
        "pd.productDetailImages, " +
        "pd.productColors, " +
        "pb.eventApplyCount) " +
        "FROM ProductBase pb  " +
        "JOIN ProductDetail pd ON pb.productDetail.id = pd.id " +
        "WHERE pb.id = :id")
    Optional<ProductInfo> getProductDetailInfoById(@Param("id") Long id);

    boolean existsProductBaseByCategoryId(Long id);

    @Query("select new rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo(" +
        "pb.id, " +
        "pb.name, " +
        "pb.price, " +
        "pb.mainThumbnailImage, " +
        "pb.subThumbnailImage," +
        "pb.discountPrice, " +
        "pb.displayOrder, " +
        "pb.visible, " +
        "pb.category.id, " +
        "pb.bundle.id, " +
        "pb.event.id, " +
        "pb.eventApplyCount," +
        "pb.soldCount) " +
        "from ProductBase pb WHERE pb.event.id = :id ORDER BY pb.displayOrder DESC")
    Page<SimpleProductInfo> getProductInfoByBundleId(@Param("id") Long id, Pageable pageable);

    @Query("select new rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo(" +
        "pb.id, " +
        "pb.name, " +
        "pb.price, " +
        "pb.mainThumbnailImage, " +
        "pb.subThumbnailImage," +
        "pb.discountPrice, " +
        "pb.displayOrder, " +
        "pb.visible, " +
        "pb.category.id, " +
        "pb.bundle.id, " +
        "pb.event.id, " +
        "pb.eventApplyCount, " +
        "pb.soldCount) " +
        "from ProductBase pb WHERE pb.category.id = :id ORDER BY pb.displayOrder DESC")
    Page<SimpleProductInfo> getProductInfoByCategoryId(@Param("id") Long id, Pageable pageable);

    @Query("select new rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo(" +
        "pb.id, " +
        "pb.name, " +
        "pb.price, " +
        "pb.mainThumbnailImage, " +
        "pb.subThumbnailImage," +
        "pb.discountPrice, " +
        "pb.displayOrder, " +
        "pb.visible, " +
        "pb.category.id, " +
        "pb.bundle.id, " +
        "pb.event.id, " +
        "pb.eventApplyCount, " +
        "pb.soldCount) " +
        "from ProductBase pb WHERE pb.event.id = :id ORDER BY pb.displayOrder DESC")
    Page<SimpleProductInfo> getProductInfoByEventId(@Param("id") Long id, Pageable pageable);

    @Query(
        "select new rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo(" +
            "pb.id, " +
            "pb.name, " +
            "pb.price, " +
            "pb.mainThumbnailImage, " +
            "pb.subThumbnailImage," +
            "pb.discountPrice, " +
            "pb.displayOrder, " +
            "pb.visible, " +
            "pb.category.id, " +
            "pb.bundle.id, " +
            "pb.event.id, " +
            "pb.eventApplyCount, " +
            "pb.soldCount) " +
            "from ProductBase pb " +
            "LEFT OUTER JOIN OrderProduct op ON pb.id = op.product.id " +
            "GROUP BY pb.id " +
            "ORDER BY COALESCE(COUNT(op.id), 0) DESC"
    )
    Page<SimpleProductInfo> getPopularProductInfos(Pageable pageable);

    @Query(
        "select new rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo(" +
            "pb.id, " +
            "pb.name, " +
            "pb.price, " +
            "pb.mainThumbnailImage, " +
            "pb.subThumbnailImage," +
            "pb.discountPrice, " +
            "pb.displayOrder, " +
            "pb.visible, " +
            "pb.category.id, " +
            "pb.bundle.id, " +
            "pb.event.id, " +
            "pb.eventApplyCount, " +
            "pb.soldCount) " +
            "from ProductBase pb " +
            "LEFT OUTER JOIN OrderProduct op ON pb.id = op.product.id " +
            "WHERE pb.visible = :visible " +
            "GROUP BY pb.id " +
            "ORDER BY COALESCE(COUNT(op.id), 0) DESC"
    )
    Page<SimpleProductInfo> getPopularProductInfosByVisibility(@Param("visible") boolean visible, Pageable pageable);
}
