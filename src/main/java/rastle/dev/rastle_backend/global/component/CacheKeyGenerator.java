package rastle.dev.rastle_backend.global.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import rastle.dev.rastle_backend.domain.product.dto.GetProductRequest;
import rastle.dev.rastle_backend.global.common.constants.MethodConstants;
import rastle.dev.rastle_backend.global.util.CustomPageRequest;

import java.lang.reflect.Method;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheKeyGenerator implements KeyGenerator {
    private final ObjectMapper objectMapper;
    @Override
    public Object generate(Object target, Method method, Object... params) {
        switch (method.getName()) {
            case MethodConstants.GET_EVENT_PRODUCTS -> {
                // productService String visible, Pageable pageable
                String visible = (String) params[0];
                CustomPageRequest pageable = (CustomPageRequest) params[1];
                try {
                    return visible + " " + objectMapper.writeValueAsString(pageable);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

            }
            case MethodConstants.GET_PRODUCTS_OF_BUNDLE -> {
                // bundleService Long bundleId
                return (Long) params[0];
            }
            case MethodConstants.GET_PRODUCT_DETAIL -> {
                // productService Long id
                return (Long) params[0];
            }
            case MethodConstants.GET_PRODUCT_INFOS -> {
                // productService GetProductRequest getProductRequest
                GetProductRequest getProductRequest = (GetProductRequest) params[0];
                try {
                    return objectMapper.writeValueAsString(getProductRequest);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }default -> {
                log.warn("can't create redis cache key {} {}", target.getClass().getSimpleName(), method.getName());
                return "unknown key";
            }
        }
    }

}
