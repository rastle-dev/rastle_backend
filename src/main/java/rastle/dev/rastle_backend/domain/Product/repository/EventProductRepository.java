package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.dto.EventProductInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.model.ProductBase;

import java.util.List;
import java.util.Optional;

public interface EventProductRepository extends JpaRepository<ProductBase, Long> {

        boolean existsByEventId(Long id);

        @Query("select new rastle.dev.rastle_backend.domain.Product.dto.EventProductInfo(" +
                        "e.id, " +
                        "e.name, " +
                        "e.eventStartDate, " +
                        "e.eventEndDate, " +
                        "e.imageUrls, " +
                        "e.description, " +
                        "e.visible, " +
                        "ep.id, " +
                        "ep.name, " +
                        "ep.price, " +
                        "ep.mainThumbnailImage, " +
                        "ep.subThumbnailImage, " +
                        "ep.discountPrice, " +
                        "ep.displayOrder, " +
                        "ep.visible) " +
                        "FROM ProductBase ep " +
                        "JOIN Event e ON e.id = ep.event.id " +
                        "WHERE :lowerBound <= e.id AND e.id <= :upperBound ORDER BY ep.displayOrder ASC")
        List<EventProductInfo> getEventProducts(@Param("lowerBound") Long lowerBound,
                        @Param("upperBound") Long upperBound);

        @Query("select new rastle.dev.rastle_backend.domain.Product.dto.EventProductInfo(" +
                        "e.id, " +
                        "e.name, " +
                        "e.eventStartDate, " +
                        "e.eventEndDate, " +
                        "e.imageUrls, " +
                        "e.description, " +
                        "e.visible, " +
                        "ep.id, " +
                        "ep.name, " +
                        "ep.price, " +
                        "ep.mainThumbnailImage, " +
                        "ep.subThumbnailImage, " +
                        "ep.discountPrice, " +
                        "ep.displayOrder, " +
                        "ep.visible) " +
                        "FROM ProductBase ep " +
                        "JOIN Event e ON e.id = ep.event.id " +
                        "WHERE e.visible = :visible AND " +
                        ":lowerBound <= e.id AND" +
                        " e.id <= :upperBound ORDER BY ep.displayOrder ASC")
        List<EventProductInfo> getEventProductByVisibility(@Param("visible") boolean visible,
                        @Param("lowerBound") Long lowerBound, @Param("upperBound") Long upperBound);

        @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
                "ep.id, " +
                "ep.name, " +
                "ep.price, " +
                "ep.mainThumbnailImage, " +
                "ep.subThumbnailImage," +
                "ep.discountPrice, " +
                "ep.displayOrder, " +
                "ep.visible, " +
                "ep.category.id, " +
                "ep.bundle.id, " +
                "ep.event.id) " +
                "from ProductBase ep " +
                "WHERE ep.event.id = :id ORDER BY ep.displayOrder ASC")
        List<SimpleProductInfo> getEventProductInfosByEventId(@Param("id") Long id);

        @Query("select new rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo(" +
                "ep.id, " +
                "ep.name, " +
                "ep.price, " +
                "ep.mainThumbnailImage, " +
                "ep.subThumbnailImage," +
                "ep.discountPrice, " +
                "ep.displayOrder, " +
                "ep.visible, " +
                "ep.category.id, " +
                "ep.bundle.id, " +
                "ep.event.id) " +
                "from ProductBase ep " +
                "WHERE ep.id = :id")
        Optional<SimpleProductInfo> getEventProductInfoById(@Param("id") Long id);
}
