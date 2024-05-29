package rastle.dev.rastle_backend.domain.delivery.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.global.component.DeliveryTracker;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeliveryScheduler {
    private final DeliveryTracker deliveryTracker;
    private final OrderProductRepository orderProductRepository;

    @Scheduled(cron = "0 0 3 * * ?")
    public void updateWebHook() {
        List<String> trackingNumberOfNotDelivered = orderProductRepository.findTrackingNumberOfNotDelivered();
        for (String trackingNumber : trackingNumberOfNotDelivered) {
            deliveryTracker.registerWebHook(trackingNumber);
        }
    }
}
