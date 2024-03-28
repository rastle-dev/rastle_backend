package rastle.dev.rastle_backend.domain.delivery.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.domain.delivery.repository.DeliveryRepository;
import rastle.dev.rastle_backend.global.component.DeliveryTracker;

@Component
@RequiredArgsConstructor
public class DeliveryScheduler {
    private final DeliveryTracker deliveryTracker;
    private final DeliveryRepository deliveryRepository;

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
    public void updateWebHook() {

    }
}
