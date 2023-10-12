package rastle.dev.rastle_backend.domain.Admin.application;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.Admin.exception.NotEmptyBundleException;
import rastle.dev.rastle_backend.domain.Admin.exception.NotEmptyCategoryException;
import rastle.dev.rastle_backend.domain.Admin.exception.NotEmptyEventException;
import rastle.dev.rastle_backend.domain.Bundle.dto.BundleDTO.BundleCreateRequest;
import rastle.dev.rastle_backend.domain.Bundle.dto.BundleDTO.BundleUpdateRequest;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto.CategoryCreateRequest;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto.CategoryUpdateRequest;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryInfo;
import rastle.dev.rastle_backend.domain.Category.model.Category;
import rastle.dev.rastle_backend.domain.Category.repository.CategoryRepository;
import rastle.dev.rastle_backend.domain.Event.dto.EventDTO.EventCreateRequest;
import rastle.dev.rastle_backend.domain.Event.dto.EventDTO.EventUpdateRequest;
import rastle.dev.rastle_backend.domain.Event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.Event.model.Event;
import rastle.dev.rastle_backend.domain.Event.repository.EventRepository;
import rastle.dev.rastle_backend.domain.Bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.Bundle.model.Bundle;
import rastle.dev.rastle_backend.domain.Bundle.repository.BundleRepository;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.MemberInfoDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.MemberInfoDto.OrderDetail;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.MemberInfoDto.OrderProductDetail;
import rastle.dev.rastle_backend.domain.Member.model.Member;
import rastle.dev.rastle_backend.domain.Member.repository.MemberRepository;
import rastle.dev.rastle_backend.domain.Orders.model.Orders;
import rastle.dev.rastle_backend.domain.Orders.repository.OrderRepository;
import rastle.dev.rastle_backend.domain.Product.dto.ColorInfo;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductCreateRequest;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductCreateResult;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductUpdateRequest;
import rastle.dev.rastle_backend.domain.Product.dto.ProductImageInfo;
import rastle.dev.rastle_backend.domain.Product.model.*;
import rastle.dev.rastle_backend.domain.Product.repository.*;
import rastle.dev.rastle_backend.global.component.S3Component;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstant.*;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final EventProductRepository eventProductRepository;
    private final BundleRepository bundleRepository;
    private final BundleProductRepository bundleProductRepository;
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
    public ProductCreateResult createProduct(ProductCreateRequest createRequest) {
        ProductBase saved;
        HashMap<String, Color> colorToSave = new HashMap<>();
        List<Size> sizeToSave = new ArrayList<>();
        Category category = categoryRepository.findById(createRequest.getCategoryId())
                .orElseThrow(NotFoundByIdException::new);
        Bundle bundle = null;
        Event event = null;
        if (createRequest.getBundleId() != null) {
            bundle = bundleRepository.findById(createRequest.getBundleId())
                    .orElseThrow(NotFoundByIdException::new);
        }

        if (createRequest.getEventId() != null) {
            event = eventRepository.findById(createRequest.getEventId()).orElseThrow(NotFoundByIdException::new);
        }
        ProductBase productBase = createRequest.toProductBase(category, bundle, event);
        saved = productBaseRepository.save(productBase);
        setColorAndSize(createRequest.getColorAndSizes(), colorToSave, sizeToSave, saved);
        colorRepository.saveAll(colorToSave.values());
        sizeRepository.saveAll(sizeToSave);

        return toCreateResult(saved, createRequest, event, bundle);
    }

    private ProductCreateResult toCreateResult(ProductBase saved,
                                               ProductCreateRequest createRequest, Event event, Bundle bundle) {
        ProductCreateResult createResult = ProductCreateResult.builder()
                .id(saved.getId())
                .name(saved.getName())
                .isEvent(saved.isEventProduct())
                .categoryId(createRequest.getCategoryId())
                .colorAndSizes(createRequest.getColorAndSizes())
                .price(saved.getPrice())
                .discountPrice(saved.getDiscountPrice())
                .displayOrder(saved.getDisplayOrder())
                .visible(saved.isVisible())
                .build();
        if (event != null) {
            createResult.setEventId(event.getId());
        }
        if (bundle != null) {
            createResult.setBundleId(bundle.getId());
        }
        return createResult;
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
        String mainThumbnailUrl = s3Component.uploadSingleImageToS3(MAIN_THUMBNAIL, mainThumbnail);
        productBase.setMainThumbnailImage(mainThumbnailUrl);

        return ProductImageInfo.builder()
                .productBaseId(productBase.getId())
                .imageUrls(List.of(mainThumbnailUrl))
                .build();
    }

    @Transactional
    public ProductImageInfo uploadSubThumbnail(Long id, MultipartFile subThumbnail) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        String subThumbnailUrl = s3Component.uploadSingleImageToS3(SUB_THUMBNAIL,subThumbnail);
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

        List<Image> images = s3Component.uploadAndGetImageList(MAIN_IMAGE, mainImages, mainImage);
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

        List<Image> images = s3Component.uploadAndGetImageList(DETAIL_IMAGE, detailImages, detailImage);
        imageRepository.saveAll(images);

        return ProductImageInfo.builder()
                .productBaseId(productBase.getId())
                .imageUrls(images.stream().map(Image::getImageUrl).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public ProductUpdateRequest updateProductInfo(Long id, ProductUpdateRequest updateRequest) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        if (updateRequest.getVisible() != null) {
            productBase.setVisible(updateRequest.getVisible());
        }
        if (updateRequest.getDiscountPrice() != null) {
            productBase.setDiscountPrice(updateRequest.getDiscountPrice());
        }
        if (updateRequest.getDisplayOrder() != null) {
            productBase.setDisplayOrder(updateRequest.getDisplayOrder());
        }
        if (updateRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategoryId()).orElseThrow(NotFoundByIdException::new);
            productBase.setCategory(category);
        }
        if (updateRequest.getColorAndSizes() != null) {
            List<Color> colors = productBase.getColors();
            colorRepository.deleteAll(colors);

            HashMap<String, Color> colorToSave = new HashMap<>();
            List<Size> sizeToSave = new ArrayList<>();

            setColorAndSize(updateRequest.getColorAndSizes(), colorToSave, sizeToSave, productBase);

            colorRepository.saveAll(colorToSave.values());
            sizeRepository.saveAll(sizeToSave);
        }
        if (updateRequest.getPrice() != null) {
            productBase.setPrice(updateRequest.getPrice());
        }
        if (updateRequest.getName() != null) {
            productBase.setName(updateRequest.getName());
        }
        if (updateRequest.getBundleId() != null) {
            Bundle bundle = bundleRepository.findById(updateRequest.getBundleId()).orElseThrow(NotFoundByIdException::new);
            productBase.setBundle(bundle);
        }
        if (updateRequest.getEventId() != null) {
            Event event = eventRepository.findById(updateRequest.getEventId()).orElseThrow(NotFoundByIdException::new);
            productBase.setEvent(event);
        }

        return updateRequest;
    }


    @Transactional
    public ProductImageInfo updateMainThumbnail(Long id, MultipartFile mainThumbnail) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        s3Component.deleteImageByUrl(productBase.getMainThumbnailImage());
        String mainThumbnailUrl = s3Component.uploadSingleImageToS3(MAIN_THUMBNAIL, mainThumbnail);
        productBase.setMainThumbnailImage(mainThumbnailUrl);

        return ProductImageInfo.builder()
                .productBaseId(productBase.getId())
                .imageUrls(List.of(mainThumbnailUrl))
                .build();
    }

    @Transactional
    public ProductImageInfo updateSubThumbnail(Long id, MultipartFile subThumbnail) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        s3Component.deleteImageByUrl(productBase.getSubThumbnailImage());
        String subThumbnailUrl = s3Component.uploadSingleImageToS3(SUB_THUMBNAIL, subThumbnail);
        productBase.setSubThumbnailImage(subThumbnailUrl);

        return ProductImageInfo.builder()
                .productBaseId(productBase.getId())
                .imageUrls(List.of(subThumbnailUrl))
                .build();
    }

    @Transactional
    public ProductImageInfo updateMainImages(Long id, List<MultipartFile> mainImages) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        List<Image> toDelete = productBase.getMainImage().getImages();
        ProductImage mainImage = productBase.getMainImage();
        return updateImage(mainImages, productBase, toDelete, mainImage, MAIN_IMAGE);
    }

    @Transactional
    public ProductImageInfo updateDetailImages(Long id, List<MultipartFile> detailImages) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        List<Image> toDelete = productBase.getDetailImage().getImages();
        ProductImage detailImage = productBase.getDetailImage();
        return updateImage(detailImages, productBase, toDelete, detailImage, DETAIL_IMAGE);
    }

    private ProductImageInfo updateImage(List<MultipartFile> detailImages, ProductBase productBase, List<Image> toDelete, ProductImage detailImage, String imageType) {
        for (Image image : toDelete) {
            s3Component.deleteImageByUrl(image.getImageUrl());
        }

        List<Image> images = s3Component.uploadAndGetImageList(imageType,detailImages, detailImage);
        imageRepository.saveAll(images);

        return ProductImageInfo.builder()
                .productBaseId(productBase.getId())
                .imageUrls(images.stream().map(Image::getImageUrl).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public String deleteProduct(Long id) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);

        s3Component.deleteImageByUrl(productBase.getMainThumbnailImage());
        s3Component.deleteImageByUrl(productBase.getSubThumbnailImage());
        List<Image> detailImages = productBase.getDetailImage().getImages();
        List<Image> mainImages = productBase.getMainImage().getImages();

        for (Image image : detailImages) {
            s3Component.deleteImageByUrl(image.getImageUrl());
        }

        for (Image image : mainImages) {
            s3Component.deleteImageByUrl(image.getImageUrl());
        }

        productBaseRepository.delete(productBase);
        return "DELETED";
    }

    // ==============================================================================================================
    // 상품세트 관련 서비스
    // ==============================================================================================================
    @Transactional
    public BundleInfo createBundle(BundleCreateRequest createRequest) {
        Bundle newBundle = Bundle.builder()
                .name(createRequest.getName())
                .saleStartTime(TimeUtil.convertStringToLocalDateTime(createRequest.getStartDate(),
                        createRequest.getStartHour(), createRequest.getStartMinute(), createRequest.getStartSecond()))
                .description(createRequest.getDescription())
                .visible(createRequest.getVisible())
                .build();
        bundleRepository.save(newBundle);
        return newBundle.toBundleInfo();
    }

    @Transactional
    public BundleInfo uploadBundleImages(Long id, List<MultipartFile> images) {
        Bundle bundle =  bundleRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        String imageUrls = s3Component.uploadImagesAndGetString(BUNDLE_IMAGE, images);
        bundle.setImageUrls(imageUrls);

        return BundleInfo.builder()
                .id(bundle.getId())
                .saleStartTime(bundle.getSaleStartTime())
                .name(bundle.getName())
                .imageUrls(bundle.getImageUrls())
                .description(bundle.getDescription())
                .build();
    }

    @Transactional
    public BundleUpdateRequest updateBundle(Long id, BundleUpdateRequest bundleUpdateRequest) {
        Bundle bundle = bundleRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        if (bundleUpdateRequest.getVisible() != null) {
            bundle.setVisible(bundleUpdateRequest.getVisible());
        }
        if (bundleUpdateRequest.getName() != null) {
            bundle.setName(bundleUpdateRequest.getName());
        }
        if (bundleUpdateRequest.getDescription() != null) {
            bundle.setDescription(bundleUpdateRequest.getDescription());
        }
        if (bundleUpdateRequest.getStartDate() != null) {
            bundle.setSaleStartTime(TimeUtil.convertStringToLocalDateTime(
                    bundleUpdateRequest.getStartDate(),
                    bundleUpdateRequest.getStartHour(),
                    bundleUpdateRequest.getStartMinute(),
                    bundleUpdateRequest.getStartSecond(
                    )));
        }

        return bundleUpdateRequest;
    }

    @Transactional
    public BundleInfo updateBundleImages(Long id, List<MultipartFile> images) {
        Bundle bundle = bundleRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        String[] imageUrls = bundle.getImageUrls().split(",");
        for (String imageUrl : imageUrls) {
            if (imageUrl.length() > 1) {
                s3Component.deleteImageByUrl(imageUrl);
            }
        }

        String imageUrlString = s3Component.uploadImagesAndGetString(BUNDLE_IMAGE, images);
        bundle.setImageUrls(imageUrlString);

        return BundleInfo.builder()
                .id(bundle.getId())
                .saleStartTime(bundle.getSaleStartTime())
                .name(bundle.getName())
                .imageUrls(bundle.getImageUrls())
                .description(bundle.getDescription())
                .build();
    }

    @Transactional
    public String deleteBundle(Long id) {
        if (bundleProductRepository.existsBundleProductByBundleId(id)) {
            throw new NotEmptyBundleException();
        }
        bundleRepository.deleteById(id);
        return "DELETED";
    }

    // ==============================================================================================================
    // 카테고리 관련 서비스
    // ==============================================================================================================
    @Transactional
    public CategoryInfo createCategory(CategoryCreateRequest createRequest) {
        Category saved = categoryRepository.save(createRequest.toEntity());

        return CategoryInfo.builder()
                .id(saved.getId())
                .name(saved.getName())
                .build();
    }

    @Transactional
    public CategoryInfo updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        category.setName(categoryUpdateRequest.getName());
        return CategoryInfo.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }


    @Transactional
    public String deleteCategory(Long id) {
        if (productBaseRepository.existsProductBaseByCategoryId(id)) {
            throw new NotEmptyCategoryException();
        }
        categoryRepository.deleteById(id);
        return "DELETED";
    }


    // ==============================================================================================================
    // 이벤트 관련 서비스
    // ==============================================================================================================
    @Transactional
    public EventInfo createEvent(EventCreateRequest createRequest) {
        Event newEvent = Event.builder()
                .name(createRequest.getName())
                .eventStartDate(TimeUtil.convertStringToLocalDateTime(createRequest.getStartDate(),
                        createRequest.getStartHour(), createRequest.getStartMinute(), createRequest.getStartSecond()))
                .eventEndDate(TimeUtil.convertStringToLocalDateTime(createRequest.getEndDate(),
                        createRequest.getEndHour(), createRequest.getEndMinute(), createRequest.getEndSecond()))
                .description(createRequest.getDescription())
                .visible(createRequest.getVisible())
                .build();
        eventRepository.save(newEvent);
        return newEvent.toEventInfo();
    }

    @Transactional
    public EventInfo uploadEventImages(Long id, List<MultipartFile> images) {
        Event event =  eventRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        String imageUrls = s3Component.uploadImagesAndGetString(EVENT_IMAGE, images);
        event.setImageUrls(imageUrls);

        return EventInfo.builder()
                .id(event.getId())
                .startDate(event.getEventStartDate())
                .endDate(event.getEventEndDate())
                .name(event.getName())
                .imageUrls(event.getImageUrls())
                .description(event.getDescription())
                .visible(event.isVisible())
                .build();
    }

    @Transactional
    public EventUpdateRequest updateEvent(Long id, EventUpdateRequest eventUpdateRequest) {
        Event event =  eventRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        if (eventUpdateRequest.getVisible() != null) {
            event.setVisible(eventUpdateRequest.getVisible());
        }
        if (eventUpdateRequest.getDescription() != null) {
            event.setDescription(eventUpdateRequest.getDescription());
        }
        if (eventUpdateRequest.getName() != null) {
            event.setName(eventUpdateRequest.getName());
        }
        if (eventUpdateRequest.getEndDate() != null) {
            event.setEventEndDate(TimeUtil.convertStringToLocalDateTime(
                    eventUpdateRequest.getEndDate(),
                    eventUpdateRequest.getEndHour(),
                    eventUpdateRequest.getEndMinute(),
                    eventUpdateRequest.getEndSecond()
            ));
        }
        if (eventUpdateRequest.getStartDate() != null) {
            event.setEventStartDate(TimeUtil.convertStringToLocalDateTime(
                    eventUpdateRequest.getStartDate(),
                    eventUpdateRequest.getStartHour(),
                    eventUpdateRequest.getStartMinute(),
                    eventUpdateRequest.getStartSecond(
                    )));
        }

        return eventUpdateRequest;
    }

    @Transactional
    public EventInfo updateEventImages(Long id, List<MultipartFile> images) {
        Event event =  eventRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        String[] imageUrls = event.getImageUrls().split(",");
        for (String image : imageUrls) {
            if (image.length() > 1) {
                s3Component.deleteImageByUrl(image);
            }
        }

        String imageUrlString = s3Component.uploadImagesAndGetString(EVENT_IMAGE, images);
        event.setImageUrls(imageUrlString);


        return EventInfo.builder()
                .id(event.getId())
                .startDate(event.getEventStartDate())
                .endDate(event.getEventEndDate())
                .name(event.getName())
                .imageUrls(event.getImageUrls())
                .description(event.getDescription())
                .visible(event.isVisible())
                .build();
    }

    @Transactional
    public String deleteEvent(Long id) {
        if (eventProductRepository.existsByEventId(id)) {
            throw new NotEmptyEventException();
        }
        eventRepository.deleteById(id);
        return "DELETED";
    }

    // ==============================================================================================================
    // 멤버 관련 서비스
    // ==============================================================================================================
    @Transactional
    public Page<MemberInfoDto> getAllMembers(Pageable pageable) {
        Page<Member> members = memberRepository.findAllUsers(pageable);
        return members.map(this::convertMemberToMemberInfoDto);
    }

    @Transactional
    public MemberInfoDto getMemberByEmail(String email) {
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.orElseThrow(NotFoundByIdException::new);
            return convertMemberToMemberInfoDto(member);
        } else {
            return null;
        }
    }

    private MemberInfoDto convertMemberToMemberInfoDto(Member member) {
        List<OrderDetail> allOrderDetails = convertOrdersToOrderDetails(orderRepository.findByMemberId(member.getId()));
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
    }

    private List<OrderDetail> convertOrdersToOrderDetails(List<Orders> orders) {
        return orders.stream().flatMap(order -> {
            List<OrderProductDetail> orderProductDetails = order.getOrderProduct().stream().map(op -> {
                return OrderProductDetail.builder()
                        .color(op.getColor())
                        .size(op.getSize())
                        .count(op.getCount())
                        .productName(op.getProduct().getName())
                        .build();
            }).collect(Collectors.toList());

            return Stream.of(OrderDetail.builder()
                    .orderId(order.getId())
                    .orderProducts(orderProductDetails)
                    .build());
        }).collect(Collectors.toList());
    }

}
