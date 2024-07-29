package rastle.dev.rastle_backend.domain.product.repository.mysql;

import jakarta.persistence.QueryHint;

import org.hibernate.annotations.Cache;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.product.dto.EventProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

import java.util.List;
import java.util.Optional;

public interface EventProductRepository extends JpaRepository<ProductBase, Long> {

    boolean existsByEventId(Long id);

    @Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select new rastle.dev.rastle_backend.domain.product.dto.EventProductInfo(" +
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
        "ep.visible, " +
        "ep.link) " +
        "FROM ProductBase ep " +
        "JOIN Event e ON e.id = ep.event.id ")
    List<EventProductInfo> getEventProducts(Pageable pageable);

    @Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select new rastle.dev.rastle_backend.domain.product.dto.EventProductInfo(" +
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
        "ep.visible, " +
        "ep.link) " +
        "FROM ProductBase ep " +
        "JOIN Event e ON e.id = ep.event.id " +
        "WHERE e.visible = :visible ")
    List<EventProductInfo> getEventProductByVisibility(@Param("visible") boolean visible,
                                                       Pageable pageable);

    @Query("select new rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo(" +
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
        "ep.event.id," +
        "ep.eventApplyCount, " +
        "ep.soldOut, " +
        "ep.soldCount) " +
        "from ProductBase ep " +
        "WHERE ep.event.id = :id ORDER BY ep.displayOrder ASC")
    List<SimpleProductInfo> getEventProductInfosByEventId(@Param("id") Long id);

    @Query("select new rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo(" +
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
        "ep.event.id, " +
        "ep.eventApplyCount," +
        "ep.soldOut, " +
        "ep.soldCount) " +
        "from ProductBase ep " +
        "WHERE ep.id = :id")
    Optional<SimpleProductInfo> getEventProductInfoById(@Param("id") Long id);
}
