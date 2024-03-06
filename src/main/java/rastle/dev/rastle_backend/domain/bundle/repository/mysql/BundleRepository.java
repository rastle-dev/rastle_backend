package rastle.dev.rastle_backend.domain.bundle.repository.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.bundle.model.Bundle;

public interface BundleRepository extends JpaRepository<Bundle, Long> {
    @Query("select new rastle.dev.rastle_backend.domain.bundle.dto.BundleInfo(b.id, " +
        "b.name, " +
        "b.imageUrls, " +
        "b.description, " +
        "b.saleStartTime, " +
        "b.visible) " +
        "from Bundle b")
    Page<BundleInfo> getBundles(Pageable pageable);

    @Query("select new rastle.dev.rastle_backend.domain.bundle.dto.BundleInfo(b.id, " +
        "b.name, " +
        "b.imageUrls, " +
        "b.description, " +
        "b.saleStartTime, " +
        "b.visible) " +
        "from Bundle b WHERE b.visible = :visible")
    Page<BundleInfo> getBundlesByVisibility(@Param("visible") boolean visible, Pageable pageable);

}
