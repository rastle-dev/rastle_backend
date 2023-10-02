package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.model.ProductBase;

public interface ProductBaseRepository extends JpaRepository<ProductBase, Long> {

    @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
            "pb.id, " +
            "pb.name, " +
            "pb.price, " +
            "pb.mainThumbnailImage, " +
            "pb.subThumbnailImage," +
            "pb.isEventProduct, " +
            "pb.discountPrice, " +
            "pb.displayOrder, " +
            "pb.visible, " +
            "pb.category.id) " +
            "from ProductBase pb ORDER BY pb.displayOrder ASC")
    Page<SimpleProductInfo> getProductInfos(Pageable pageable);
    @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
            "pb.id, " +
            "pb.name, " +
            "pb.price, " +
            "pb.mainThumbnailImage, " +
            "pb.subThumbnailImage," +
            "pb.isEventProduct, " +
            "pb.discountPrice, " +
            "pb.displayOrder, " +
            "pb.visible, " +
            "pb.category.id) " +
            "from ProductBase pb WHERE pb.visible = :visible ORDER BY pb.displayOrder ASC")
    Page<SimpleProductInfo> getProductInfosByVisibility(@Param("visible") boolean visible, Pageable pageable);

    @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
            "pb.id, " +
            "pb.name, " +
            "pb.price, " +
            "pb.mainThumbnailImage, " +
            "pb.subThumbnailImage," +
            "pb.isEventProduct, " +
            "pb.discountPrice, " +
            "pb.displayOrder, " +
            "pb.visible, " +
            "pb.category.id) " +
            "from ProductBase pb " +
            "where pb.id = :id")
    SimpleProductInfo getProductInfoById(@Param("id") Long id);

    boolean existsProductBaseByCategoryId(Long id);
}
