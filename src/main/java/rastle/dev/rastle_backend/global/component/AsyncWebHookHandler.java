package rastle.dev.rastle_backend.global.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.delivery.dto.request.WebHookRequest;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.global.common.enums.DeliveryTrackerStatus;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncWebHookHandler {
    private final DeliveryTracker deliveryTracker;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    @Async("deliveryTrackerTaskExecutor")
    public void handleWebHook(WebHookRequest webHookRequest) {
        log.info("handle webhook request {}", webHookRequest.getTrackingNumber());
        DeliveryTrackerStatus deliveryStatus = deliveryTracker.getDeliveryStatus(webHookRequest.getTrackingNumber());
        log.info("delivery status : {}", deliveryStatus.getStatus());
        List<OrderProduct> byTrackingNumber = orderProductRepository.findByTrackingNumber(webHookRequest.getTrackingNumber());
        for (OrderProduct orderProduct : byTrackingNumber) {
            if (deliveryStatus.equals(DeliveryTrackerStatus.DELIVERED)) {
                orderProduct.updateOrderStatus(OrderStatus.DELIVERED);
                orderProduct.updateDeliveryTime(LocalDateTime.now());
            }
            if (deliveryStatus.equals(DeliveryTrackerStatus.IN_TRANSIT)
                || deliveryStatus.equals(DeliveryTrackerStatus.OUT_FOR_DELIVERY)
                || deliveryStatus.equals(DeliveryTrackerStatus.AT_PICKUP)
                || deliveryStatus.equals(DeliveryTrackerStatus.AVAILABLE_FOR_PICKUP)
                || deliveryStatus.equals(DeliveryTrackerStatus.INFORMATION_RECEIVED)) {
                orderProduct.updateOrderStatus(OrderStatus.DELIVERY_STARTED);
            }
            if (deliveryStatus.equals(DeliveryTrackerStatus.UNKNOWN)) {
                orderProduct.updateOrderStatus(OrderStatus.DELIVERY_UNKNOWN);
            }
            if (deliveryStatus.equals(DeliveryTrackerStatus.EXCEPTION)) {
                orderProduct.updateOrderStatus(OrderStatus.DELIVERY_EXCEPTION);
            }
            if (deliveryStatus.equals(DeliveryTrackerStatus.ATTEMPT_FAIL)) {
                orderProduct.updateOrderStatus(OrderStatus.DELIVERY_FAILED);
            }

        }
    }

}
