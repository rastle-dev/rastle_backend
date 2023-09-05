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
    private final EventRepository eventRepository;
    private final MarketRepository marketRepository;
    private final MarketProductRepository marketProductRepository;
    private final EventProductRepository eventProductRepository;
    private final SizeRepository sizeRepository;
    private final ProductImageRepository productImageRepository;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final S3Component s3Component;

    @Transactional
    public ProductCreateResult createProduct(ProductCreateRequest createRequest) {
        ProductBase saved;
        HashMap<String, Color> colorToSave = new HashMap<>();
        List<Size> sizeToSave = new ArrayList<>();
        Category category = categoryRepository.findById(createRequest.getCategoryId()).orElseThrow(NotFoundByIdException::new);
        if (createRequest.isEventCategory()) {
            Event event = eventRepository.findById(createRequest.getMarketId()).orElseThrow(NotFoundByIdException::new);
            EventProduct eventProduct = createRequest.toEventProduct(event, category);
            saved = eventProductRepository.save(eventProduct);
        } else {
            Market market = marketRepository.findById(createRequest.getMarketId()).orElseThrow(NotFoundByIdException::new);
            MarketProduct marketProduct = createRequest.toMarketProduct(market, category);
            saved = marketProductRepository.save(marketProduct);
        }
        setColorAndSize(createRequest.getColorAndSizes(), colorToSave, sizeToSave, saved);
        colorRepository.saveAll(colorToSave.values());
        sizeRepository.saveAll(sizeToSave);

        return toCreateResult(saved, createRequest);

    }

    private ProductCreateResult toCreateResult(ProductBase saved, ProductCreateRequest createRequest) {
        return ProductCreateResult.builder()
                .id(saved.getId())
                .name(saved.getName())
                .isEvent(saved.isEventProduct())
                .categoryId(createRequest.getCategoryId())
                .colorAndSizes(createRequest.getColorAndSizes())
                .price(saved.getPrice())
                .discount(saved.getDiscount())
                .build();

    }

    private void setColorAndSize(List<ColorInfo> colorInfos, HashMap<String, Color> colorToSave, List<Size> sizeToSave, ProductBase saved) {
        for (ColorInfo colorAndSize : colorInfos) {
            Color color = colorToSave.getOrDefault(colorAndSize.getColor(), new Color(colorAndSize.getColor(), saved));
            sizeToSave.add(new Size(colorAndSize.getSize(), colorAndSize.getCount(), color));
            colorToSave.put(colorAndSize.getColor(), color);
        }
    }


    @Transactional
    public ProductImageInfo uploadMainThumbnail(Long id, MultipartFile mainThumbnail) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        String mainThumbnailUrl = s3Component.uploadSingleImageToS3(mainThumbnail);
        productBase.setMainThumbnailImage(mainThumbnailUrl);


        return ProductImageInfo.builder()
                .productBaseId(productBase.getId())
                .imageUrls(List.of(mainThumbnailUrl))
                .build();
    }


    @Transactional
    public ProductImageInfo uploadSubThumbnail(Long id, MultipartFile subThumbnail) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        String subThumbnailUrl = s3Component.uploadSingleImageToS3(subThumbnail);
        productBase.setSubThumbnailImage(subThumbnailUrl);


        return ProductImageInfo.builder()
                .productBaseId(productBase.getId())
                .imageUrls(List.of(subThumbnailUrl))
                .build();
    }


    @Transactional
    public ProductImageInfo uploadMainImages(Long id, List<MultipartFile> mainImages) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);

        ProductImage mainImage = new ProductImage();
        productImageRepository.save(mainImage);

        productBase.setMainImage(mainImage);

        List<Image> images = s3Component.uploadAndGetImageList(mainImages, mainImage);
        imageRepository.saveAll(images);


        return ProductImageInfo.builder()
                .productBaseId(productBase.getId())
                .imageUrls(images.stream().map(Image::getImageUrl).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public ProductImageInfo uploadDetailImages(Long id, List<MultipartFile> detailImages) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);

        ProductImage detailImage = new ProductImage();
        productImageRepository.save(detailImage);

        productBase.setDetailImage(detailImage);

        List<Image> images = s3Component.uploadAndGetImageList(detailImages, detailImage);
        imageRepository.saveAll(images);

        return ProductImageInfo.builder()
                .productBaseId(productBase.getId())
                .imageUrls(images.stream().map(Image::getImageUrl).collect(Collectors.toList()))
                .build();
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
