package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.model.ProductBase;

import java.util.Optional;

public interface ProductBaseRepository extends JpaRepository<ProductBase, Long> {

    @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
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
            "pb.event.id) " +
            "from ProductBase pb WHERE pb.event.id = null ORDER BY pb.displayOrder ASC")
    Page<SimpleProductInfo> getProductInfos(Pageable pageable);
    @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
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
            "pb.event.id) " +
            "from ProductBase pb WHERE pb.visible = :visible AND pb.event.id = null ORDER BY pb.displayOrder ASC")
    Page<SimpleProductInfo> getProductInfosByVisibility(@Param("visible") boolean visible, Pageable pageable);

    @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
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
            "pb.event.id) " +
            "from ProductBase pb " +
            "where pb.id = :id and pb.event.id = null")
    Optional<SimpleProductInfo> getProductInfoById(@Param("id") Long id);

    boolean existsProductBaseByCategoryId(Long id);

    @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
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
            "pb.event.id) " +
            "from ProductBase pb WHERE pb.event.id = :id ORDER BY pb.displayOrder ASC")
    Page<SimpleProductInfo> getProductInfoByBundleId(@Param("id") Long id, Pageable pageable);

    @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
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
            "pb.event.id) " +
            "from ProductBase pb WHERE pb.category.id = :id ORDER BY pb.displayOrder ASC")
    Page<SimpleProductInfo> getProductInfoByCategoryId(@Param("id") Long id, Pageable pageable);

    @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
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
            "pb.event.id) " +
            "from ProductBase pb WHERE pb.event.id = :id ORDER BY pb.displayOrder ASC")
    Page<SimpleProductInfo> getProductInfoByEventId(@Param("id") Long id, Pageable pageable);

}
