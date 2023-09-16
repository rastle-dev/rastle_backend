package rastle.dev.rastle_backend.domain.Admin.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryInfo;
import rastle.dev.rastle_backend.domain.Category.model.Category;
import rastle.dev.rastle_backend.domain.Category.repository.CategoryRepository;
import rastle.dev.rastle_backend.domain.Event.dto.EventDTO;
import rastle.dev.rastle_backend.domain.Event.model.Event;
import rastle.dev.rastle_backend.domain.Event.repository.EventRepository;
import rastle.dev.rastle_backend.domain.Market.dto.MarketDTO;
import rastle.dev.rastle_backend.domain.Market.model.Market;
import rastle.dev.rastle_backend.domain.Market.repository.MarketRepository;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.MemberInfoDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.MemberInfoDto.OrderDetail;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.MemberInfoDto.OrderProductDetail;
import rastle.dev.rastle_backend.domain.Member.model.Member;
import rastle.dev.rastle_backend.domain.Member.repository.MemberRepository;
import rastle.dev.rastle_backend.domain.Orders.model.Orders;
import rastle.dev.rastle_backend.domain.Orders.repository.OrderRepository;
import rastle.dev.rastle_backend.domain.Product.dto.ColorInfo;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO;
import rastle.dev.rastle_backend.domain.Product.dto.ProductImageInfo;
import rastle.dev.rastle_backend.domain.Product.model.*;
import rastle.dev.rastle_backend.domain.Product.repository.*;
import rastle.dev.rastle_backend.global.component.S3Component;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final EventProductRepository eventProductRepository;
    private final MarketRepository marketRepository;
    private final MarketProductRepository marketProductRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ProductBaseRepository productBaseRepository;
    private final S3Component s3Component;
    private final ProductImageRepository productImageRepository;
    private final ImageRepository imageRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    // ==============================================================================================================
    // 상품 관련 서비스
    // ==============================================================================================================
    @Transactional
    public ProductDTO.ProductCreateResult createProduct(ProductDTO.ProductCreateRequest createRequest) {
        ProductBase saved;
        HashMap<String, Color> colorToSave = new HashMap<>();
        List<Size> sizeToSave = new ArrayList<>();
        Category category = categoryRepository.findById(createRequest.getCategoryId())
                .orElseThrow(NotFoundByIdException::new);

        if (createRequest.isEventCategory()) {
            Event event = eventRepository.findById(createRequest.getMarketId()).orElseThrow(NotFoundByIdException::new);
            EventProduct eventProduct = createRequest.toEventProduct(event, category);
            saved = eventProductRepository.save(eventProduct);
        } else {
            Market market = marketRepository.findById(createRequest.getMarketId())
                    .orElseThrow(NotFoundByIdException::new);
            MarketProduct marketProduct = createRequest.toMarketProduct(market, category);
            saved = marketProductRepository.save(marketProduct);
        }

        setColorAndSize(createRequest.getColorAndSizes(), colorToSave, sizeToSave, saved);
        colorRepository.saveAll(colorToSave.values());
        sizeRepository.saveAll(sizeToSave);

        return toCreateResult(saved, createRequest);
    }

    private ProductDTO.ProductCreateResult toCreateResult(ProductBase saved,
            ProductDTO.ProductCreateRequest createRequest) {
        return ProductDTO.ProductCreateResult.builder()
                .id(saved.getId())
                .name(saved.getName())
                .isEvent(saved.isEventProduct())
                .categoryId(createRequest.getCategoryId())
                .colorAndSizes(createRequest.getColorAndSizes())
                .price(saved.getPrice())
                .discount(saved.getDiscount())
                .build();
    }

    private void setColorAndSize(List<ColorInfo> colorInfos, HashMap<String, Color> colorToSave, List<Size> sizeToSave,
            ProductBase saved) {
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

    // ==============================================================================================================
    // 마켓 관련 서비스
    // ==============================================================================================================
    @Transactional
    public String createMarket(MarketDTO.MarketCreateRequest createRequest) {
        Market newMarket = Market.builder()
                .name(createRequest.getName())
                .saleStartTime(TimeUtil.convertStringToLocalDateTime(createRequest.getStartDate(),
                        createRequest.getStartHour(), createRequest.getStartMinute(), createRequest.getStartSecond()))
                .build();
        marketRepository.save(newMarket);
        return "CREATED";
    }

    // ==============================================================================================================
    // 카테고리 관련 서비스
    // ==============================================================================================================
    @Transactional
    public CategoryInfo createCategory(CategoryDto.CategoryCreateRequest createRequest) {
        Category saved = categoryRepository.save(createRequest.toEntity());

        return CategoryInfo.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }

    @Transactional
    public CategoryInfo uploadImages(Long id, List<MultipartFile> images) {
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        String imageUrls = s3Component.uploadImagesAndGetString(images);
        category.setImageUrls(imageUrls);

        return CategoryInfo.builder()
                .id(category.getId())
                .name(category.getName())
                .imageUrls(category.getImageUrls())
                .build();
    }

    // ==============================================================================================================
    // 이벤트 관련 서비스
    // ==============================================================================================================
    @Transactional
    public String createEvent(EventDTO.EventCreateRequest createRequest) {
        Event newEvent = Event.builder()
                .name(createRequest.getName())
                .eventStartDate(TimeUtil.convertStringToLocalDateTime(createRequest.getStartDate(),
                        createRequest.getStartHour(), createRequest.getStartMinute(), createRequest.getStartSecond()))
                .eventEndDate(TimeUtil.convertStringToLocalDateTime(createRequest.getEndDate(),
                        createRequest.getEndHour(), createRequest.getEndMinute(), createRequest.getEndSecond()))
                .build();
        eventRepository.save(newEvent);
        return "CREATED";
    }

    // ==============================================================================================================
    // 멤버 관련 서비스
    // ==============================================================================================================
    public List<MemberInfoDto> getAllMembers() {
        List<Member> members = memberRepository.findAll();

        return members.stream().map(member -> {
            List<OrderDetail> allOrderDetails = new ArrayList<>();
            List<Orders> orders = orderRepository.findByMemberId(member.getId());

            if (!orders.isEmpty()) {
                for (Orders order : orders) {
                    List<OrderProductDetail> orderProductDetails = order.getOrderProduct().stream().map(op -> {
                        return OrderProductDetail.builder()
                                .color(op.getColor())
                                .size(op.getSize())
                                .count(op.getCount())
                                .productName(op.getProduct().getName())
                                .build();
                    }).collect(Collectors.toList());

                    allOrderDetails.add(OrderDetail.builder()
                            .orderId(order.getId())
                            .orderProducts(orderProductDetails)
                            .build());
                }
            }

            return MemberInfoDto.builder()
                    .email(member.getEmail())
                    .userLoginType(member.getUserLoginType())
                    .userName(member.getUserName())
                    .phoneNumber(member.getPhoneNumber())
                    .address(String.format("%s %s %s", member.getZipCode(), member.getRoadAddress(),
                            member.getDetailAddress()))
                    .createdDate(member.getCreatedDate())
                    .allOrderDetails(allOrderDetails)
                    .build();
        }).collect(Collectors.toList());
    }
}
