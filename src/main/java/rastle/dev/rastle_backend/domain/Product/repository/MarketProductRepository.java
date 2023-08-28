package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.model.MarketProduct;

import java.time.LocalDateTime;

public interface MarketProductRepository extends JpaRepository<MarketProduct, Long> {
    @Query(
            "select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
                    "mp.id, " +
                    "mp.name, " +
                    "mp.price, " +
                    "mp.mainThumbnailImage, " +
                    "mp.subThumbnailImage, " +
                    "mp.isEventProduct) " +
                    "from MarketProduct mp " +
                    "join Market m on m.id = mp.market.id " +
                    "where m.saleStartTime <= :currentTime " +
                    "and :currentTime <= m.saleEndTime"
    )
    Page<SimpleProductInfo> getCurrentMarketProducts(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);
    @Query(
            "select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
                    "mp.id, " +
                    "mp.name, " +
                    "mp.price, " +
                    "mp.mainThumbnailImage, " +
                    "mp.subThumbnailImage, " +
                    "mp.isEventProduct) " +
                    "from MarketProduct mp " +
                    "join Market m on m.id = mp.market.id " +
                    "where m.saleEndTime <= :currentTime "
    )
    Page<SimpleProductInfo> getPastMarketProducts(@Param("currentTime") LocalDateTime currentTime, Pageable pageable);
}
