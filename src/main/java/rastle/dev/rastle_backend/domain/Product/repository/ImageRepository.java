package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.model.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query(
            "select i.imageUrl from Image i where i.productImage.id = :id"
    )
    List<String> findImageUrlByProductImageId(@Param("id") Long id);
}
