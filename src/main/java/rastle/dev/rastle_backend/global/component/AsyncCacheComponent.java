package rastle.dev.rastle_backend.global.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.event.model.Event;
import rastle.dev.rastle_backend.domain.event.repository.mysql.EventRepository;
import rastle.dev.rastle_backend.domain.product.dto.EventProductDetailInfo.EventProductDetailOutInfo;
import rastle.dev.rastle_backend.domain.product.dto.*;
import rastle.dev.rastle_backend.domain.product.repository.mysql.BundleProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.EventProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductBaseRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductQRepository;
import rastle.dev.rastle_backend.global.cache.ObjectRedisTemplate;
import rastle.dev.rastle_backend.global.common.annotation.WriteThroughCache;
import rastle.dev.rastle_backend.global.common.constants.CacheConstant;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.util.CustomPageRequest;

import java.util.Set;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.ALL;
import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.TRUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncCacheComponent {
    private final ObjectRedisTemplate objectRedisTemplate;
    private final ProductBaseRepository productBaseRepository;
    private final EventRepository eventRepository;
    private final BundleProductRepository bundleProductRepository;
    private final EventProductRepository eventProductRepository;
    private final ProductQRepository productQRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    @Async("cacheTaskExecutor")
    public void writeThroughCache(WriteThroughCache writeThroughCache, Object[] args, String[] parameterNames) {

        if (isUpdateProductRequest(writeThroughCache)) {
            Long id = getId(parameterNames, args, writeThroughCache.identifier());
            updateProductDetail(id);

        }
        updateProductByBundle();
        updateGetProducts();
        updateGetEventProducts();
    }

    private void updateProductDetail(Long id) {
        if (id == null) {
            return;
        }
        String cacheKey = CacheConstant.GET_PRODUCT_DETAIL + "::" + id;
        ProductInfo productInfo = productBaseRepository.getProductDetailInfoById(id).orElseThrow(NotFoundByIdException::new);
        if (productInfo.getEventId() != null) {
            Event event = eventRepository.findById(productInfo.getEventId()).orElseThrow(() -> new RuntimeException("이벤트 아이디로 존재하는 이벤트가 없음"));
            EventProductDetailOutInfo.fromProductInfo(productInfo, event);
            objectRedisTemplate.save(cacheKey, EventProductDetailOutInfo.fromProductInfo(productInfo, event));
        } else {
            objectRedisTemplate.save(cacheKey, productInfo);
        }

    }

    private void updateProductByBundle() {
        String cacheKey = CacheConstant.GET_PRODUCTS_BY_BUNDLE;
        Set<String> keys = getRedisKeySet(cacheKey);
        for (String key : keys) {
            String value = parseKey(cacheKey, key);
            Long bundleId = Long.parseLong(value);
            SimpleProductQueryResult queryResult = new SimpleProductQueryResult(bundleProductRepository.getBundleProductInfosByBundleId(bundleId));
            objectRedisTemplate.save(key, queryResult);
        }
    }

    private void updateGetProducts() {
        String cacheKey = CacheConstant.GET_PRODUCTS;
        Set<String> keys = getRedisKeySet(cacheKey);
        for (String key : keys) {
            String value = parseKey(cacheKey, key);
            try {
                GetProductRequest getProductRequest = objectMapper.readValue(value, GetProductRequest.class);
                objectRedisTemplate.save(key, productQRepository.getProductInfos(getProductRequest));
            } catch (JsonProcessingException e) {
                log.info(e.getMessage());
                log.info("error during parsing json object of GetProductRequest");
            }


        }
    }

    private void updateGetEventProducts() {
        String cacheKey = CacheConstant.GET_EVENT_PRODUCTS;
        Set<String> keys = getRedisKeySet(cacheKey);
        for (String key : keys) {
            String value = parseKey(cacheKey, key);
            String[] getConditions = value.split(" ");
            String visible = getConditions[0];
            try {
                CustomPageRequest pageRequest = objectMapper.readValue(getConditions[1], CustomPageRequest.class);
                EventProductQueryResult eventProductQueryResult;
                if (visible.equals(ALL)) {
                    eventProductQueryResult = new EventProductQueryResult(eventProductRepository.getEventProducts(CustomPageRequest.toPageable(pageRequest)).stream().map(EventProductInfo::toOutInfo).toList());
                } else if (visible.equals(TRUE)) {
                    eventProductQueryResult = new EventProductQueryResult(eventProductRepository.getEventProductByVisibility(true, CustomPageRequest.toPageable(pageRequest)).stream().map(EventProductInfo::toOutInfo).toList());
                } else {
                    eventProductQueryResult = new EventProductQueryResult(eventProductRepository.getEventProductByVisibility(false, CustomPageRequest.toPageable(pageRequest)).stream().map(EventProductInfo::toOutInfo).toList());
                }
                objectRedisTemplate.save(key, eventProductQueryResult);
            } catch (JsonProcessingException e) {
                log.info(e.getMessage());
                log.info("error during parsing json object of Pageable");
            }

        }

    }

    private Set<String> getRedisKeySet(String key) {
        Set<String> set = objectRedisTemplate.keys(key + "::*");
        if (set == null) {
            return Set.of();
        }
        return set;
    }

    private String parseKey(String key, String toParse) {
        return toParse.substring(key.length() + 2);
    }

    private boolean isUpdateProductRequest(WriteThroughCache writeThroughCache) {
        return writeThroughCache.paramClassType().equals(Object.class);
    }

    private Long getId(String[] parameterNames, Object[] args, String identifier) {
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(identifier)) {
                return (Long) args[i];
            }
        }
        log.info("there is no given parameter id");
        return null;
    }
}
