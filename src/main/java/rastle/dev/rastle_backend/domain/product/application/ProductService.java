package rastle.dev.rastle_backend.domain.product.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.event.model.Event;
import rastle.dev.rastle_backend.domain.event.repository.mysql.EventRepository;
import rastle.dev.rastle_backend.domain.product.dto.EventProductDetailInfo.EventProductDetailOutInfo;
import rastle.dev.rastle_backend.domain.product.dto.*;
import rastle.dev.rastle_backend.domain.product.repository.mysql.EventProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductBaseRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductQRepository;
import rastle.dev.rastle_backend.global.common.annotation.WriteThroughCache;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.util.CustomPageRequest;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.ALL;
import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.TRUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductBaseRepository productBaseRepository;
    private final EventProductRepository eventProductRepository;
    private final EventRepository eventRepository;
    private final ProductQRepository productQRepository;


//    @Cacheable(cacheNames = GET_PRODUCTS, cacheManager = "cacheManager", keyGenerator = "cacheKeyGenerator")
    @Transactional(readOnly = true)
    public SimpleProductQueryResult getProductInfos(GetProductRequest getProductRequest) {

        return productQRepository.getProductInfos(getProductRequest);
    }

//    @Cacheable(cacheNames = GET_PRODUCT_DETAIL, cacheManager = "cacheManager", keyGenerator = "cacheKeyGenerator")
    @Transactional(readOnly = true)
    public Object getProductDetail(Long id) {
        ProductInfo productInfo = productBaseRepository.getProductDetailInfoById(id).orElseThrow(NotFoundByIdException::new);
        if (productInfo.getEventId() != null) {
            Event event = eventRepository.findById(productInfo.getEventId()).orElseThrow(() -> new RuntimeException("이벤트 아이디로 존재하는 이벤트가 없음"));
            return EventProductDetailOutInfo.fromProductInfo(productInfo, event);
        }
        return productInfo;
    }

//    @Cacheable(cacheNames = GET_EVENT_PRODUCTS, cacheManager = "cacheManager", keyGenerator = "cacheKeyGenerator")
    @Transactional(readOnly = true)
    public EventProductQueryResult getEventProducts(String visible, CustomPageRequest pageRequest) {
        if (visible.equals(ALL)) {
            return new EventProductQueryResult(eventProductRepository.getEventProducts(CustomPageRequest.toPageable(pageRequest)).stream().map(EventProductInfo::toOutInfo).toList());
        } else if (visible.equals(TRUE)) {
            return new EventProductQueryResult(eventProductRepository.getEventProductByVisibility(true, CustomPageRequest.toPageable(pageRequest)).stream().map(EventProductInfo::toOutInfo).toList());
        } else {
            return new EventProductQueryResult(eventProductRepository.getEventProductByVisibility(false, CustomPageRequest.toPageable(pageRequest)).stream().map(EventProductInfo::toOutInfo).toList());
        }

    }

    @WriteThroughCache
    public void testCache(Long id, CustomPageRequest customPageRequest) {
        log.info(String.valueOf(customPageRequest.getPage()));
        log.info(String.valueOf(customPageRequest.getSize()));
        log.info("business logic");
    }
}
