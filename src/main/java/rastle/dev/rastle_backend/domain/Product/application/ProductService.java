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
import rastle.dev.rastle_backend.domain.Event.model.Event;
import rastle.dev.rastle_backend.domain.Event.repository.EventRepository;
import rastle.dev.rastle_backend.domain.Market.model.Market;
import rastle.dev.rastle_backend.domain.Market.repository.MarketRepository;
import rastle.dev.rastle_backend.domain.Product.dto.ColorInfo;
import rastle.dev.rastle_backend.domain.Product.model.*;
import rastle.dev.rastle_backend.domain.Product.repository.*;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductCreateRequest;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.error.exception.S3ImageUploadException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public ProductCreateResult createProduct(ProductCreateRequest createRequest) {
        ProductBase saved;
        HashMap<String, Color> colorToSave = new HashMap<>();
        List<Size> sizeToSave = new ArrayList<>();
        if (createRequest.isEventCategory()) {
            Event event = eventRepository.findById(createRequest.getCategoryId()).orElseThrow(NotFoundByIdException::new);
            EventProduct eventProduct = EventProduct.builder()
                    .name(createRequest.getName())
                    .price(createRequest.getPrice())
                    .isEventProduct(true)
                    .discount(createRequest.getDiscount())
                    .event(event)
                    .build();
            saved = eventProductRepository.save(eventProduct);
        } else {
            Market market = marketRepository.findById(createRequest.getCategoryId()).orElseThrow(NotFoundByIdException::new);
            MarketProduct marketProduct = MarketProduct.builder()
                    .name(createRequest.getName())
                    .price(createRequest.getPrice())
                    .isEventProduct(false)
                    .discount(createRequest.getDiscount())
                    .market(market)
                    .build();
            saved = marketProductRepository.save(marketProduct);
        }
        for (ColorInfo colorAndSize : createRequest.getColorAndSizes()) {
            Color color = colorToSave.getOrDefault(colorAndSize.getColor(), new Color(colorAndSize.getColor(), saved));
            sizeToSave.add(new Size(colorAndSize.getSize(), colorAndSize.getCount(), color));
            colorToSave.put(colorAndSize.getColor(), color);

        }
        colorRepository.saveAll(colorToSave.values());
        sizeRepository.saveAll(sizeToSave);

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

    @Transactional
    public String uploadMainThumbnail(Long id, MultipartFile mainThumbnail) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        String mainThumbnailUrl = uploadImageToS3(mainThumbnail);
        productBase.setMainThumbnailImage(mainThumbnailUrl);


        return "SAVED_MAIN_THUMBNAIL";
    }


    @Transactional
    public String uploadSubThumbnail(Long id, MultipartFile subThumbnail) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        String subThumbnailUrl = uploadImageToS3(subThumbnail);
        productBase.setSubThumbnailImage(subThumbnailUrl);


        return "SAVED_SUB_THUMBNAIL";
    }


    @Transactional
    public String uploadMainImages(Long id, List<MultipartFile> mainImages) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);

        ProductImage mainImage = new ProductImage();
        productImageRepository.save(mainImage);

        productBase.setMainImage(mainImage);

        List<Image> images = new ArrayList<>();
        for (MultipartFile mainImageFiles : mainImages) {
            Image newImage = new Image(uploadImageToS3(mainImageFiles), mainImage);
            images.add(newImage);
        }
        imageRepository.saveAll(images);

        return "SAVED_MAIN_IMAGES";
    }

    @Transactional
    public String uploadDetailImages(Long id, List<MultipartFile> detailImages) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);

        ProductImage detailImage = new ProductImage();
        productImageRepository.save(detailImage);

        productBase.setDetailImage(detailImage);

        List<Image> images = new ArrayList<>();
        for (MultipartFile mainImageFiles : detailImages) {
            Image newImage = new Image(uploadImageToS3(mainImageFiles), detailImage);
            images.add(newImage);
        }
        imageRepository.saveAll(images);

        return "SAVED_DETAIL_IMAGES";
    }


    private String uploadImageToS3(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        try {

            amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            log.info("Exception during uploading image to S3");
            throw new S3ImageUploadException();
        }
        return amazonS3.getUrl(bucket, fileName).toString();
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
