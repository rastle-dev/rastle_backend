package rastle.dev.rastle_backend.domain.Product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.Product.dto.ColorInfo;
import rastle.dev.rastle_backend.domain.Product.model.*;
import rastle.dev.rastle_backend.domain.Product.repository.*;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import java.time.LocalDateTime;
import java.util.*;
import static rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.*;
import static rastle.dev.rastle_backend.global.common.constants.CommonConstant.ALL;
import static rastle.dev.rastle_backend.global.common.constants.CommonConstant.TRUE;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductBaseRepository productBaseRepository;
    private final ColorRepository colorRepository;
    private final BundleProductRepository bundleProductRepository;
    private final EventProductRepository eventProductRepository;
    private final ImageRepository imageRepository;

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
    public SimpleProductInfo getProductDetail(Long id) {
        return productBaseRepository.getProductInfoById(id);
    }

    @Transactional(readOnly = true)
    public List<ColorInfo> getProductColors(Long id) {
        return colorRepository.findColorInfoByProductId(id);
    }

    @Transactional(readOnly = true)
    public ProductImages getProductImages(Long id) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return ProductImages.builder()
                .mainImages(imageRepository.findImageUrlByProductImageId(productBase.getMainImage().getId()))
                .detailImages(imageRepository.findImageUrlByProductImageId(productBase.getDetailImage().getId()))
                .build();
    }

    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getBundleProducts(String visible, Long lowerBound, Long upperBound, Pageable pageable) {
        return bundleProductRepository.getBundleProducts(LocalDateTime.now(), pageable);
    }


    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getEventProducts(String visible, Long lowerBound, Long upperBound, Pageable pageable) {
        return eventProductRepository.getEventProducts(pageable);
    }
}
