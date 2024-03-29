package rastle.dev.rastle_backend.global.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.domain.delivery.dto.request.WebHookRequest;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.global.common.enums.DeliveryTrackerStatus;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncWebHookHandler {
    private final DeliveryTracker deliveryTracker;
    private final OrderProductRepository orderProductRepository;

    @Async("webhookTaskExecutor")
    public void handleWebHook(WebHookRequest webHookRequest) {
        DeliveryTrackerStatus deliveryStatus = deliveryTracker.getDeliveryStatus(webHookRequest.getTrackingNumber());
        Optional<OrderProduct> byTrackingNumber = orderProductRepository.findByTrackingNumber(webHookRequest.getTrackingNumber());
        if (byTrackingNumber.isPresent()) {
            OrderProduct orderProduct = byTrackingNumber.get();
            if (deliveryStatus.equals(DeliveryTrackerStatus.DELIVERED)) {
                orderProduct.updateOrderStatus(OrderStatus.DELIVERED);
            }
            if (deliveryStatus.equals(DeliveryTrackerStatus.IN_TRANSIT)
            || deliveryStatus.equals(DeliveryTrackerStatus.OUT_FOR_DELIVERY)
            || deliveryStatus.equals(DeliveryTrackerStatus.AT_PICKUP)
            || deliveryStatus.equals(DeliveryTrackerStatus.AVAILABLE_FOR_PICKUP)) {
                orderProduct.updateOrderStatus(OrderStatus.DELIVERY_STARTED);
            }

        }
    }
}
