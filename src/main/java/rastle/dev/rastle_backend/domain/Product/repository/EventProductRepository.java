package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.dto.EventProductInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.Product.model.EventProduct;

import java.util.List;

public interface EventProductRepository extends JpaRepository<EventProduct, Long> {
    @Query(
            "select new rastle.dev.rastle_backend.domain.Product.dto.EventProductInfo(" +
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
                    "ep.discount, " +
                    "ep.displayOrder, " +
                    "ep.visible) " +
                    "FROM EventProduct ep " +
                    "JOIN Event e ON e.id = ep.event.id " +
                    "WHERE :lowerBound <= e.id AND e.id <= :upperBound"
    )
    List<EventProductInfo> getEventProducts(@Param("lowerBound") Long lowerBound, @Param("upperBound") Long upperBound);
    @Query(
            "select new rastle.dev.rastle_backend.domain.Product.dto.EventProductInfo(" +
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
                    "ep.discount, " +
                    "ep.displayOrder, " +
                    "ep.visible) " +
                    "FROM EventProduct ep " +
                    "JOIN Event e ON e.id = ep.event.id " +
                    "WHERE e.visible = :visible AND " +
                    ":lowerBound <= e.id AND" +
                    " e.id <= :upperBound"
    )
    List<EventProductInfo> getEventProductByVisibility(@Param("visible") boolean visible, @Param("lowerBound") Long lowerBound, @Param("upperBound") Long upperBound);
}
