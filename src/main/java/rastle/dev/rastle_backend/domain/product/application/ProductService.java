package rastle.dev.rastle_backend.domain.product.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.event.model.Event;
import rastle.dev.rastle_backend.domain.event.repository.mysql.EventRepository;
import rastle.dev.rastle_backend.domain.product.dto.*;
import rastle.dev.rastle_backend.domain.product.repository.mysql.BundleProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.EventProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductBaseRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductQRepository;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

import java.util.List;

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

    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getProductInfos(GetProductRequest getProductRequest) {

        return productQRepository.getProductInfos(getProductRequest);
    }

    @Transactional(readOnly = true)
    public Object getProductDetail(Long id) throws JsonProcessingException {
        ProductInfo productInfo = productBaseRepository.getProductDetailInfoById(id).orElseThrow(NotFoundByIdException::new);
        if (productInfo.getEventId() != null) {
            Event event = eventRepository.findById(productInfo.getEventId()).orElseThrow(() -> new RuntimeException("이벤트 아이디로 존재하는 이벤트가 없음"));
            return EventProductDetailInfo.fromProductInfo(productInfo, event);
        }
        return productInfo;
    }

    @Transactional(readOnly = true)
    public List<BundleProductInfo> getBundleProducts(String visible, Long lowerBound, Long upperBound) {
        if (visible.equals(ALL)) {
            return bundleProductRepository.getBundleProducts(lowerBound, upperBound);
        } else if (visible.equals(TRUE)) {
            return bundleProductRepository.getBundleProductsByVisibility(true, lowerBound, upperBound);
        } else {
            return bundleProductRepository.getBundleProductsByVisibility(false, lowerBound, upperBound);
        }
    }

    @Transactional(readOnly = true)
    public List<EventProductInfo> getEventProducts(String visible, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (visible.equals(ALL)) {
            return eventProductRepository.getEventProducts(pageable);
        } else if (visible.equals(TRUE)) {
            return eventProductRepository.getEventProductByVisibility(true, pageable);
        } else {
            return eventProductRepository.getEventProductByVisibility(false, pageable);
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
