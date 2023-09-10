package rastle.dev.rastle_backend.domain.Product.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.Category.model.Category;
import rastle.dev.rastle_backend.domain.Category.repository.CategoryRepository;
import rastle.dev.rastle_backend.domain.Event.model.Event;
import rastle.dev.rastle_backend.domain.Event.repository.EventRepository;
import rastle.dev.rastle_backend.domain.Market.model.Market;
import rastle.dev.rastle_backend.domain.Market.repository.MarketRepository;
import rastle.dev.rastle_backend.domain.Product.dto.ColorInfo;
import rastle.dev.rastle_backend.domain.Product.dto.ProductImageInfo;
import rastle.dev.rastle_backend.domain.Product.model.*;
import rastle.dev.rastle_backend.domain.Product.repository.*;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductCreateRequest;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.component.S3Component;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.error.exception.S3ImageUploadException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductBaseRepository productBaseRepository;
    private final ColorRepository colorRepository;
    private final MarketProductRepository marketProductRepository;
    private final EventProductRepository eventProductRepository;
    private final ImageRepository imageRepository;


    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getProductInfos(Pageable pageable) {

        return productBaseRepository.getProductInfos(pageable);
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
    public Page<SimpleProductInfo> getCurrentMarketProducts(Pageable pageable) {
        return marketProductRepository.getCurrentMarketProducts(LocalDateTime.now(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getPastMarketProducts(Pageable pageable) {
        return marketProductRepository.getPastMarketProducts(LocalDateTime.now(), pageable);

    }

    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getEventMarketProducts(Pageable pageable) {
        return eventProductRepository.getEventProducts(pageable);
    }
}
