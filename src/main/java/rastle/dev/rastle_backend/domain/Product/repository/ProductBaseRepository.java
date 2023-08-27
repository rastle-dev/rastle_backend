package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDetail;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.model.ProductBase;

import java.util.List;
import java.util.Optional;

public interface ProductBaseRepository extends JpaRepository<ProductBase, Long> {

    @Query(
        "select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
                "pb.id, " +
                "pb.name, " +
                "pb.price, " +
                "pb.mainThumbnailImage, " +
                "pb.subThumbnailImage," +
                "pb.isEventProduct) " +
                "from ProductBase pb"
    )
    List<SimpleProductInfo> getProductInfos();
    @Query(
            "select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
                    "pb.id, " +
                    "pb.name, " +
                    "pb.price, " +
                    "pb.mainThumbnailImage, " +
                    "pb.subThumbnailImage," +
                    "pb.isEventProduct) " +
                    "from ProductBase pb " +
                    "where pb.id = :id"
    )
    SimpleProductInfo getProductInfoById(@Param("id") Long id);
}
