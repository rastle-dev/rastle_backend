package rastle.dev.rastle_backend.global.component;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.domain.delivery.dto.request.WebHookRequest;

@Component
@RequiredArgsConstructor
public class AsyncWebHookHandler {
    private final DeliveryTracker deliveryTracker;

    @Async("webhookTaskExecutor")
    public void handleWebHook(WebHookRequest webHookRequest) {

    }
}
