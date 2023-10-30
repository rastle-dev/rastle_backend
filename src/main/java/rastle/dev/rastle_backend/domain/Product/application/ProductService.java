package rastle.dev.rastle_backend.domain.Product.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.Product.dto.*;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductDetailInfo;
import rastle.dev.rastle_backend.domain.Product.model.ProductColor;
import rastle.dev.rastle_backend.domain.Product.model.ProductImage;
import rastle.dev.rastle_backend.domain.Product.repository.*;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import java.util.*;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstant.ALL;
import static rastle.dev.rastle_backend.global.common.constants.CommonConstant.TRUE;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ObjectMapper objectMapper;
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
    public ProductDetailInfo getProductDetail(Long id) throws JsonProcessingException {
        ProductInfo productInfo = productBaseRepository.getProductDetailInfoById(id).orElseThrow(NotFoundByIdException::new);
        ProductColor productColor = objectMapper.readValue(productInfo.getProductColors(), ProductColor.class);
        ProductImage mainImage = objectMapper.readValue(productInfo.getProductMainImages(), ProductImage.class);
        ProductImage detailImage = objectMapper.readValue(productInfo.getProductDetailImages(), ProductImage.class);
        return productInfo.toDetailInfo(productColor, mainImage, detailImage);
    }

//    @Transactional(readOnly = true)
//    public List<ColorInfo> getProductColors(Long id) {
//        return colorRepository.findColorInfoByProductId(id);
//    }

//    @Transactional(readOnly = true)
//    public ProductImages getProductImages(Long id) {
//        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
//        List<String> mainImageUrls = null;
//        List<String> detailImageUrls = null;
//        if (productBase.getProductDetail() != null) {
//            mainImageUrls = imageRepository.findImageUrlByProductImageId(productBase.getProductDetail().getId());
//        }
//        if (productBase.getDetailImage() != null) {
//            detailImageUrls = imageRepository.findImageUrlByProductImageId(productBase.getDetailImage().getId());
//        }
//
//        return ProductImages.builder()
//                .mainImages(mainImageUrls)
//                .detailImages(detailImageUrls)
//                .build();
//    }

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
