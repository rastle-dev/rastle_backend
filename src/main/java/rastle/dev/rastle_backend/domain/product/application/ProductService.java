package rastle.dev.rastle_backend.domain.product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.event.model.Event;
import rastle.dev.rastle_backend.domain.event.repository.mysql.EventRepository;
import rastle.dev.rastle_backend.domain.product.dto.*;
import rastle.dev.rastle_backend.domain.product.dto.EventProductDetailInfo.EventProductDetailOutInfo;
import rastle.dev.rastle_backend.domain.product.repository.mysql.BundleProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.EventProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductBaseRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductQRepository;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.ALL;
import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.TRUE;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductBaseRepository productBaseRepository;
    private final BundleProductRepository bundleProductRepository;
    private final EventProductRepository eventProductRepository;
    private final EventRepository eventRepository;
    private final ProductQRepository productQRepository;

//    @Cacheable(cacheNames = GET_PRODUCTS, cacheManager = "cacheManager")
    @Transactional(readOnly = true)
    public SimpleProductQueryResult getProductInfos(GetProductRequest getProductRequest) {

        return productQRepository.getProductInfos(getProductRequest);
    }


//    @Cacheable(cacheNames = GET_PRODUCT_DETAIL, cacheManager = "cacheManager")
    @Transactional(readOnly = true)
    public Object getProductDetail(Long id) {
        ProductInfo productInfo = productBaseRepository.getProductDetailInfoById(id).orElseThrow(NotFoundByIdException::new);
        if (productInfo.getEventId() != null) {
            Event event = eventRepository.findById(productInfo.getEventId()).orElseThrow(() -> new RuntimeException("이벤트 아이디로 존재하는 이벤트가 없음"));
            return EventProductDetailOutInfo.fromProductInfo(productInfo, event);
        }
        return productInfo;
    }

//    @Cacheable(cacheNames = GET_BUNDLE_PRODUCTS, cacheManager = "cacheManager")
    @Transactional(readOnly = true)
    public BundleProductQueryResult getBundleProducts(String visible, Long lowerBound, Long upperBound) {
        if (visible.equals(ALL)) {
            return new BundleProductQueryResult(bundleProductRepository.getBundleProducts(lowerBound, upperBound).stream().map(BundleProductInfo::toBundleProductOutInfo).toList());
        } else if (visible.equals(TRUE)) {
            return new BundleProductQueryResult(bundleProductRepository.getBundleProductsByVisibility(true, lowerBound, upperBound).stream().map(BundleProductInfo::toBundleProductOutInfo).toList());
        } else {
            return new BundleProductQueryResult(bundleProductRepository.getBundleProductsByVisibility(false, lowerBound, upperBound).stream().map(BundleProductInfo::toBundleProductOutInfo).toList());
        }
    }

//    @Cacheable(cacheNames = GET_EVENT_PRODUCTS, cacheManager = "cacheManager")
    @Transactional(readOnly = true)
    public EventProductQueryResult getEventProducts(String visible, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (visible.equals(ALL)) {
            return new EventProductQueryResult(eventProductRepository.getEventProducts(pageable).stream().map(EventProductInfo::toOutInfo).toList());
        } else if (visible.equals(TRUE)) {
            return new EventProductQueryResult(eventProductRepository.getEventProductByVisibility(true, pageable).stream().map(EventProductInfo::toOutInfo).toList());
        } else {
            return new EventProductQueryResult(eventProductRepository.getEventProductByVisibility(false, pageable).stream().map(EventProductInfo::toOutInfo).toList());
        }

    }

    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getPopularProducts(String visible, Pageable pageable) {
        if (visible.equals(ALL)) {
            return productBaseRepository.getPopularProductInfos(pageable);
        } else if (visible.equals(TRUE)) {
            return productBaseRepository.getPopularProductInfosByVisibility(true, pageable);
        } else {
            return productBaseRepository.getPopularProductInfosByVisibility(false, pageable);

        }
    }
}
