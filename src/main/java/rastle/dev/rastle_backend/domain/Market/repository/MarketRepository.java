package rastle.dev.rastle_backend.domain.Market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.Market.dto.MarketInfo;
import rastle.dev.rastle_backend.domain.Market.model.Market;

import java.time.LocalDateTime;
import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {
    @Query(
            "select new rastle.dev.rastle_backend.domain.Market.dto.MarketInfo(m.id, m.name, m.saleStartTime, m.saleEndTime)" +
                    "from Market m where m.saleStartTime <= :currentTime and :currentTime <= m.saleEndTime"
    )
    List<MarketInfo> getCurrentMarkets(@Param("currentTime") LocalDateTime currentTime);
}
