package rastle.dev.rastle_backend.domain.Bundle.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.Bundle.model.Bundle;

import java.time.LocalDateTime;
import java.util.List;

public interface BundleRepository extends JpaRepository<Bundle, Long> {
    @Query(
            "select new rastle.dev.rastle_backend.domain.Bundle.dto.BundleInfo(b.id, " +
                    "b.name, " +
                    "b.imageUrls, " +
                    "b.description, " +
                    "b.saleStartTime)" +
                    "from Bundle b where b.saleStartTime >= :current"
    )
    Page<BundleInfo> getBundles(@Param("current") LocalDateTime current,Pageable pageable);
}
