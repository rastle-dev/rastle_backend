package rastle.dev.rastle_backend.domain.order.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;

@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderDetailRepository orderDetailRepository;

    @Scheduled(cron = "0 0 0 * * MON") // 매주 월요일 자정에 실행
    // @Scheduled(cron = "0 * * * * ?") // 매 분 0초에 실행
    @Transactional
    public void deleteReadyOrders() {
        orderDetailRepository.deleteAllReadyOrders();
    }
}
