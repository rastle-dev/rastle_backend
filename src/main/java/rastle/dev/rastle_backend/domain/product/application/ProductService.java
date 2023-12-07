package rastle.dev.rastle_backend.domain.product.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.product.dto.BundleProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.EventProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.ProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.repository.mysql.BundleProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.EventProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductBaseRepository;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

import java.util.List;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.ALL;
import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.TRUE;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ObjectMapper objectMapper;
    private final ProductBaseRepository productBaseRepository;
    private final BundleProductRepository bundleProductRepository;
    private final EventProductRepository eventProductRepository;

    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getProductInfos(String visible, Pageable pageable) {
        if (visible.equals(ALL)) {
            return productBaseRepository.getProductInfos(pageable);
        } else if (visible.equals(TRUE)) {
            return productBaseRepository.getProductInfosByVisibility(true, pageable);
        } else {
            return productBaseRepository.getProductInfosByVisibility(false, pageable);
        }
    }

    @Transactional(readOnly = true)
    public ProductInfo getProductDetail(Long id) throws JsonProcessingException {
        return productBaseRepository.getProductDetailInfoById(id).orElseThrow(NotFoundByIdException::new);
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
    public List<EventProductInfo> getEventProducts(String visible, Long lowerBound, Long upperBound) {
        if (visible.equals(ALL)) {
            return eventProductRepository.getEventProducts(lowerBound, upperBound);
        } else if (visible.equals(TRUE)) {
            return eventProductRepository.getEventProductByVisibility(true, lowerBound, upperBound);
        } else {
            return eventProductRepository.getEventProductByVisibility(false, lowerBound, upperBound);
        }

    }
}
