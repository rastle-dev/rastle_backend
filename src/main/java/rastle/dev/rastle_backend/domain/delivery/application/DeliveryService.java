package rastle.dev.rastle_backend.domain.delivery.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.delivery.dto.request.WebHookRequest;
import rastle.dev.rastle_backend.global.component.AsyncWebHookHandler;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.HANDLED;

@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final AsyncWebHookHandler asyncWebHookHandler;

    @Transactional(readOnly = true)
    public String handleWebHook(WebHookRequest webHookRequest) {
        asyncWebHookHandler.handleWebHook(webHookRequest);
        return HANDLED;
    }
}
