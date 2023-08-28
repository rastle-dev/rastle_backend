package rastle.dev.rastle_backend.domain.Product.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.Product.dto.ColorInfo;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO;
import rastle.dev.rastle_backend.domain.Product.model.*;
import rastle.dev.rastle_backend.domain.Product.repository.*;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductCreateRequest;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductBaseRepository productBaseRepository;
    private final ColorRepository colorRepository;
    private final MarketProductRepository marketProductRepository;
    private final EventProductRepository eventProductRepository;
    private final SizeRepository sizeRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public String createProduct(ProductCreateRequest createRequest) {
        MarketProduct created = MarketProduct.builder()
                .isEventProduct(false)
                .price(createRequest.getPrice())
                .name(createRequest.getName())
                .mainThumbnailImage(createRequest.getMainThumbnail())
                .subThumbnailImage(createRequest.getSubThumbnail()).build();
        marketProductRepository.save(created);
        List<String> colors = List.of("blue", "red", "black");
        List<String> sizes = List.of("S", "M", "L");
        for (String color : colors) {
            Color newColor = new Color(color, created);
            colorRepository.save(newColor);
            for (String size : sizes) {
                Size newSize = new Size(size, 10, newColor);
                sizeRepository.save(newSize);
            }
        }

        return "CREATED";
    }

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
