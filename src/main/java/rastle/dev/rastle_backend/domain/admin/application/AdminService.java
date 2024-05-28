package rastle.dev.rastle_backend.domain.admin.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.admin.dto.*;
import rastle.dev.rastle_backend.domain.admin.exception.NotEmptyBundleException;
import rastle.dev.rastle_backend.domain.admin.exception.NotEmptyCategoryException;
import rastle.dev.rastle_backend.domain.admin.exception.NotEmptyEventException;
import rastle.dev.rastle_backend.domain.admin.repository.mysql.CancelRequestQRepository;
import rastle.dev.rastle_backend.domain.admin.repository.mysql.MemberOrderQRepository;
import rastle.dev.rastle_backend.domain.bundle.dto.BundleDTO.BundleCreateRequest;
import rastle.dev.rastle_backend.domain.bundle.dto.BundleDTO.BundleUpdateRequest;
import rastle.dev.rastle_backend.domain.bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.bundle.model.Bundle;
import rastle.dev.rastle_backend.domain.bundle.repository.mysql.BundleRepository;
import rastle.dev.rastle_backend.domain.category.dto.CategoryDto.CategoryCreateRequest;
import rastle.dev.rastle_backend.domain.category.dto.CategoryDto.CategoryUpdateRequest;
import rastle.dev.rastle_backend.domain.category.dto.CategoryInfo;
import rastle.dev.rastle_backend.domain.category.model.Category;
import rastle.dev.rastle_backend.domain.category.repository.mysql.CategoryRepository;
import rastle.dev.rastle_backend.domain.event.dto.EventDTO.EventCreateRequest;
import rastle.dev.rastle_backend.domain.event.dto.EventDTO.EventUpdateRequest;
import rastle.dev.rastle_backend.domain.event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO.ProductEventApplyHistoryDTO;
import rastle.dev.rastle_backend.domain.event.model.Event;
import rastle.dev.rastle_backend.domain.event.repository.mysql.EventProductApplyRepository;
import rastle.dev.rastle_backend.domain.event.repository.mysql.EventRepository;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.MemberInfoDto;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.MemberInfoDto.OrderProductDetail;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.domain.order.model.CancelRequest;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;
import rastle.dev.rastle_backend.domain.order.repository.mysql.CancelRequestRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.product.dto.ProductDTO.ProductCreateRequest;
import rastle.dev.rastle_backend.domain.product.dto.ProductDTO.ProductCreateResult;
import rastle.dev.rastle_backend.domain.product.dto.ProductDTO.ProductUpdateRequest;
import rastle.dev.rastle_backend.domain.product.dto.ProductImageInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.domain.product.model.ProductDetail;
import rastle.dev.rastle_backend.domain.product.model.ProductImage;
import rastle.dev.rastle_backend.domain.product.repository.mysql.BundleProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.EventProductRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductBaseRepository;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductDetailRepository;
import rastle.dev.rastle_backend.global.component.DeliveryTracker;
import rastle.dev.rastle_backend.global.component.PortOneComponent;
import rastle.dev.rastle_backend.global.component.S3Component;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.util.TimeUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.*;
import static rastle.dev.rastle_backend.global.common.enums.CancelRequestStatus.COMPLETED;
import static rastle.dev.rastle_backend.global.common.enums.OrderStatus.CANCELLED;
import static rastle.dev.rastle_backend.global.common.enums.OrderStatus.PARTIALLY_CANCELLED;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final ObjectMapper objectMapper;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final EventProductRepository eventProductRepository;
    private final BundleRepository bundleRepository;
    private final BundleProductRepository bundleProductRepository;
    private final ProductBaseRepository productBaseRepository;
    private final S3Component s3Component;
    private final ProductDetailRepository productDetailRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final EventProductApplyRepository eventProductApplyRepository;
    private final MemberOrderQRepository memberOrderQRepository;
    private final OrderProductRepository orderProductRepository;
    private final CancelRequestQRepository cancelRequestQRepository;
    private final PortOneComponent portOneComponent;
    private final CancelRequestRepository cancelRequestRepository;
    private final DeliveryTracker deliveryTracker;

    // ==============================================================================================================
    // 상품 관련 서비스
    // ==============================================================================================================

    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getProductByBundleId(Long bundleId, Pageable pageable) {

        return productBaseRepository.getProductInfoByBundleId(bundleId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getProductByEventId(Long eventId, Pageable pageable) {
        return productBaseRepository.getProductInfoByEventId(eventId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SimpleProductInfo> getProductByCategoryId(Long categoryId, Pageable pageable) {
        return productBaseRepository.getProductInfoByCategoryId(categoryId, pageable);
    }

    @Transactional
    public ProductCreateResult createProduct(ProductCreateRequest createRequest) throws JsonProcessingException {
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
        productBaseRepository.save(productBase);
        ProductDetail productDetail = productDetailRepository
            .save(ProductDetail.builder().productColors(createRequest.getProductColor()).build());
        productBase.setProductDetail(productDetail);

        return toCreateResult(productBase, createRequest, event, bundle);
    }

    private ProductCreateResult toCreateResult(ProductBase saved,
                                               ProductCreateRequest createRequest, Event event, Bundle bundle) {
        ProductCreateResult createResult = ProductCreateResult.builder()
            .id(saved.getId())
            .name(saved.getName())
            .categoryId(createRequest.getCategoryId())
            .productColor(createRequest.getProductColor())
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
        String subThumbnailUrl = s3Component.uploadSingleImageToS3(SUB_THUMBNAIL, subThumbnail);
        productBase.setSubThumbnailImage(subThumbnailUrl);

        return ProductImageInfo.builder()
            .productBaseId(productBase.getId())
            .imageUrls(List.of(subThumbnailUrl))
            .build();
    }

    @Transactional
    public ProductImageInfo uploadMainImages(Long id, List<MultipartFile> mainImages) throws JsonProcessingException {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        ProductDetail productDetail = productBase.getProductDetail();

        ProductImage productImage = s3Component.uploadAndGetImageUrlList(MAIN_IMAGE, mainImages);
        productDetail.setProductMainImages(productImage);

        return ProductImageInfo.builder()
            .productBaseId(productBase.getId())
            .imageUrls(productImage.getImageUrls())
            .build();
    }

    @Transactional
    public ProductImageInfo uploadDetailImages(Long id, List<MultipartFile> detailImages)
        throws JsonProcessingException {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        ProductDetail productDetail = productBase.getProductDetail();

        ProductImage productImage = s3Component.uploadAndGetImageUrlList(DETAIL_IMAGE, detailImages);
        productDetail.setProductDetailImages(productImage);

        return ProductImageInfo.builder()
            .productBaseId(productBase.getId())
            .imageUrls(productImage.getImageUrls())
            .build();
    }

    @Transactional
    public ProductUpdateRequest updateProductInfo(Long id, ProductUpdateRequest updateRequest)
        throws JsonProcessingException {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        ProductDetail productDetail = productBase.getProductDetail();
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
            Category category = categoryRepository.findById(updateRequest.getCategoryId())
                .orElseThrow(NotFoundByIdException::new);
            productBase.setCategory(category);
        }
        if (updateRequest.getProductColor() != null) {
            productDetail.setProductColors(updateRequest.getProductColor());
        }
        if (updateRequest.getPrice() != null) {
            productBase.setPrice(updateRequest.getPrice());
        }
        if (updateRequest.getName() != null) {
            productBase.setName(updateRequest.getName());
        }
        if (updateRequest.getBundleId() != null) {
            Bundle bundle = bundleRepository.findById(updateRequest.getBundleId())
                .orElseThrow(NotFoundByIdException::new);
            productBase.setBundle(bundle);
        } else {
            productBase.setBundle(null);
        }
        if (updateRequest.getEventId() != null) {
            Event event = eventRepository.findById(updateRequest.getEventId()).orElseThrow(NotFoundByIdException::new);
            productBase.setEvent(event);
        } else {
            productBase.setEvent(null);
        }

        return updateRequest;
    }

    @Transactional
    public ProductImageInfo updateMainThumbnail(Long id, MultipartFile mainThumbnail) {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        // s3Component.deleteImageByUrl(productBase.getMainThumbnailImage());
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
        // s3Component.deleteImageByUrl(productBase.getSubThumbnailImage());
        String subThumbnailUrl = s3Component.uploadSingleImageToS3(SUB_THUMBNAIL, subThumbnail);
        productBase.setSubThumbnailImage(subThumbnailUrl);

        return ProductImageInfo.builder()
            .productBaseId(productBase.getId())
            .imageUrls(List.of(subThumbnailUrl))
            .build();
    }

    @Transactional
    public ProductImageInfo updateMainImages(Long id, List<MultipartFile> mainImages) throws JsonProcessingException {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        ProductDetail productDetail = productBase.getProductDetail();
        ProductImage mainImage = productDetail.getProductMainImages();
        return updateImage(mainImages, productBase, mainImage.getImageUrls(), productDetail, MAIN_IMAGE);

    }

    @Transactional
    public ProductImageInfo updateDetailImages(Long id, List<MultipartFile> detailImages)
        throws JsonProcessingException {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        ProductDetail productDetail = productBase.getProductDetail();
        ProductImage detailImage = productDetail.getProductDetailImages();
        return updateImage(detailImages, productBase, detailImage.getImageUrls(), productDetail, DETAIL_IMAGE);
    }

    private ProductImageInfo updateImage(List<MultipartFile> detailImages, ProductBase productBase,
                                         List<String> toDelete, ProductDetail productDetail, String imageType) throws JsonProcessingException {
        // for (String image : toDelete) {
        // s3Component.deleteImageByUrl(image);
        // }
        ProductImage productImage = s3Component.uploadAndGetImageUrlList(imageType, detailImages);
        if (imageType.equals(MAIN_IMAGE)) {
            productDetail.setProductMainImages(productImage);
        }
        if (imageType.equals(DETAIL_IMAGE)) {
            productDetail.setProductDetailImages(productImage);
        }
        return ProductImageInfo.builder()
            .productBaseId(productBase.getId())
            .imageUrls(productImage.getImageUrls())
            .build();
    }

    @Transactional
    public String deleteProduct(Long id) throws JsonProcessingException {
        ProductBase productBase = productBaseRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        ProductDetail productDetail = productBase.getProductDetail();
        ProductImage mainImage = productDetail.getProductMainImages();
        ProductImage detailImage = productDetail.getProductDetailImages();

        s3Component.deleteImageByUrl(productBase.getMainThumbnailImage());
        s3Component.deleteImageByUrl(productBase.getSubThumbnailImage());
        if (mainImage != null) {
            for (String image : mainImage.getImageUrls()) {
                s3Component.deleteImageByUrl(image);
            }

        }
        if (detailImage != null) {
            for (String image : detailImage.getImageUrls()) {
                s3Component.deleteImageByUrl(image);
            }
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
        Bundle bundle = bundleRepository.findById(id).orElseThrow(NotFoundByIdException::new);
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
                bundleUpdateRequest.getStartSecond()));
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
        Event event = eventRepository.findById(id).orElseThrow(NotFoundByIdException::new);
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
        Event event = eventRepository.findById(id).orElseThrow(NotFoundByIdException::new);
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
                eventUpdateRequest.getEndSecond()));
        }
        if (eventUpdateRequest.getStartDate() != null) {
            event.setEventStartDate(TimeUtil.convertStringToLocalDateTime(
                eventUpdateRequest.getStartDate(),
                eventUpdateRequest.getStartHour(),
                eventUpdateRequest.getStartMinute(),
                eventUpdateRequest.getStartSecond()));
        }

        return eventUpdateRequest;
    }

    @Transactional
    public EventInfo updateEventImages(Long id, List<MultipartFile> images) {
        Event event = eventRepository.findById(id).orElseThrow(NotFoundByIdException::new);
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

    /**
     * 제품 이벤트 응모 신청 내역 조회
     *
     * @param productId
     */
    @Transactional(readOnly = true)
    public Page<ProductEventApplyHistoryDTO> getProductEventApplyHistoryDTOs(Long productId, Pageable pageable) {
        return eventProductApplyRepository.getProductEventApplyHistoryDTOs(productId, pageable);
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
        List<MemberInfoDto.OrderDetail> allOrderDetails = convertOrdersToOrderDetails(
            orderDetailRepository.findByMemberId(member.getId()));
        return MemberInfoDto.builder()
            .email(member.getEmail())
            .userLoginType(member.getUserLoginType())
            .userName(member.getUserName())
            .phoneNumber(member.getPhoneNumber())
            .recipientInfo(member.getRecipientInfo())
            .createdDate(member.getCreatedDate())
            .allOrderDetails(allOrderDetails)
            .build();
    }

    private List<MemberInfoDto.OrderDetail> convertOrdersToOrderDetails(List<OrderDetail> orders) {
        return orders.stream().flatMap(order -> {
            List<OrderProductDetail> orderProductDetails = order.getOrderProduct().stream().map(op -> {
                return OrderProductDetail.builder()
                    .color(op.getColor())
                    .size(op.getSize())
                    .count(op.getCount())
                    .productName(op.getProduct().getName())
                    .build();
            }).collect(Collectors.toList());

            return Stream.of(MemberInfoDto.OrderDetail.builder()
                .orderId(order.getId())
                .orderProducts(orderProductDetails)
                .build());
        }).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public Page<GetMemberOrderInfo> getMemberOrders(GetMemberOrderCondition getMemberOrderCondition) {
        return memberOrderQRepository.getMemberOrderInfo(getMemberOrderCondition);
    }
    // TODO 여기서 웹훅 등록하는 요청도 보내야할듯
    @Transactional
    public String updateTrackingNumber(Long orderProductNumber, UpdateTrackingNumberRequest trackingNumberRequest) {
        validateTrackingNumber(trackingNumberRequest);
        OrderProduct orderProduct = orderProductRepository.findByProductOrderNumber(orderProductNumber).orElseThrow(() -> new RuntimeException("상품 주문 번호로 존재하는 상품 주문이 없다. " + orderProductNumber));
        orderProduct.updateTrackingNumber(trackingNumberRequest.getTrackingNumber());
        deliveryTracker.registerWebHook(trackingNumberRequest.getTrackingNumber());
        return UPDATED;
    }

    private void validateTrackingNumber(UpdateTrackingNumberRequest trackingNumberRequest) {
        if (trackingNumberRequest.getTrackingNumber() == null || trackingNumberRequest.getTrackingNumber().length() < 10 || trackingNumberRequest.getTrackingNumber().length() > 12) {
            throw new RuntimeException("유효하지 않은 운송장 번호 입니다. 운송장 번호는 10 ~ 12자리 문자열이어야합니다. - 같은 특수 문자 제거 필요");
        }
    }

    @Transactional(readOnly = true)
    public Page<GetCancelRequestInfo> getCancelRequest(GetCancelRequestCondition cancelRequestCondition) {
        return cancelRequestQRepository.getCancelRequestInfo(cancelRequestCondition);
    }
    @Transactional
    public CancelOrderResult cancelOrder(CancelOrderRequest cancelOrderRequest) {
        OrderProduct orderProduct = orderProductRepository.findByProductOrderNumber(cancelOrderRequest.getProductOrderNumber()).orElseThrow(() -> new RuntimeException("해당 상풍 주문 번호로 존재하는 상품 주문이 없다."));
        OrderDetail orderDetail = orderProduct.getOrderDetail();
        CancelRequest cancelRequest = cancelRequestRepository.findById(cancelOrderRequest.getCancelRequestId()).orElseThrow(() -> new RuntimeException("해당 아이디로 존재하는 취소 요청이 없습니다. " + cancelOrderRequest.getCancelRequestId()));

        PaymentResponse cancelResponse = portOneComponent.cancelPayment(cancelOrderRequest.getImpId(), cancelRequest, orderProduct);
        if (cancelResponse.getCancelAmount().equals(cancelResponse.getAmount())) {
            orderDetail.updateOrderStatus(CANCELLED);
        } else {
            orderDetail.updateOrderStatus(PARTIALLY_CANCELLED);
        }
        orderProduct.addCancelAmount(cancelRequest.getCancelAmount());
        orderProduct.initCancelRequestAmount();
        orderProduct.getOrderDetail().getPayment().addCancelledSum(orderProduct.getPrice() * cancelRequest.getCancelAmount());
        cancelRequest.updateStatus(COMPLETED);
        if (orderProduct.getCount().equals(orderProduct.getCancelAmount())) {
            orderProduct.updateOrderStatus(CANCELLED);
        } else {
            orderProduct.updateOrderStatus(PARTIALLY_CANCELLED);
        }

        return new CancelOrderResult(cancelOrderRequest.getImpId(), cancelOrderRequest.getProductOrderNumber(), orderProduct.getPrice() * cancelRequest.getCancelAmount(), cancelOrderRequest.getCancelRequestId());
    }
}
