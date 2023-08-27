package rastle.dev.rastle_backend.domain.Product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Product.dto.ColorInfo;
import rastle.dev.rastle_backend.domain.Product.model.Color;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Long> {
    List<Color> findColorsByMarketProductId(Long id);

    @Query(
            "select new rastle.dev.rastle_backend.domain.Product.dto.ColorInfo(c.name, s.name, s.count) " +
                    "from Color c " +
                    "JOIN Size s on s.color.id = c.id " +
                    "WHERE c.marketProduct.id = :id"
    )
    List<ColorInfo> findColorInfoByProductId(@Param("id") Long id);
}
