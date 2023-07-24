package rastle.dev.rastle_backend.domain.Market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Market.model.Size;

public interface SizeRepository extends JpaRepository<Size, Long> {
}
