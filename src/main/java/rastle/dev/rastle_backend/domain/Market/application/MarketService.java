package rastle.dev.rastle_backend.domain.Market.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.Market.dto.MarketDTO.MarketCreateRequest;
import rastle.dev.rastle_backend.domain.Market.dto.MarketInfo;
import rastle.dev.rastle_backend.domain.Market.model.Market;
import rastle.dev.rastle_backend.domain.Market.repository.MarketRepository;
import rastle.dev.rastle_backend.global.util.TimeUtil;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarketService {
    private final MarketRepository marketRepository;


    @Transactional
    public String createMarket(MarketCreateRequest createRequest) {
        Market newMarket = Market.builder()
                .name(createRequest.getName())
                .saleStartTime(TimeUtil.convertStringToLocalDateTime(createRequest.getStartDate(),createRequest.getStartHour(), createRequest.getStartMinute(), createRequest.getStartSecond()))
                .build();
        marketRepository.save(newMarket);
        return "CREATED";
    }

    @Transactional(readOnly = true)
    public List<MarketInfo> getCurrentMarkets() {
        LocalDateTime current = LocalDateTime.now();
        log.info(current.toString());
        return marketRepository.getCurrentMarkets(current);
    }
}
