package rastle.dev.rastle_backend.domain.Market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rastle.dev.rastle_backend.domain.Market.model.Market;

public interface MarketRepository extends JpaRepository<Market, Long> {
}
