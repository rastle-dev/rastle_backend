package rastle.dev.rastle_backend.domain.admin.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.admin.application.AdminService;
import rastle.dev.rastle_backend.domain.admin.dto.*;
import rastle.dev.rastle_backend.domain.bundle.dto.BundleDTO.BundleCreateRequest;
import rastle.dev.rastle_backend.domain.bundle.dto.BundleDTO.BundleUpdateRequest;
import rastle.dev.rastle_backend.domain.bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.category.dto.CategoryDto.CategoryCreateRequest;
import rastle.dev.rastle_backend.domain.category.dto.CategoryDto.CategoryUpdateRequest;
import rastle.dev.rastle_backend.domain.category.dto.CategoryInfo;
import rastle.dev.rastle_backend.domain.event.dto.EventDTO.EventCreateRequest;
import rastle.dev.rastle_backend.domain.event.dto.EventDTO.EventUpdateRequest;
import rastle.dev.rastle_backend.domain.event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO.ProductEventApplyHistoryDTO;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.MemberInfoDto;
import rastle.dev.rastle_backend.domain.product.dto.ProductDTO;
import rastle.dev.rastle_backend.domain.product.dto.ProductDTO.ProductUpdateRequest;
import rastle.dev.rastle_backend.domain.product.dto.ProductImageInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.common.annotation.GetExecutionTime;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import java.util.List;
import java.util.Map;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static rastle.dev.rastle_backend.global.common.enums.CancelRequestStatus.getFromIndex;

@Tag(name = "관리자 기능 API", description = "관리자 기능 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final ObjectMapper objectMapper;

    // ==============================================================================================================
    // 상품 관련 API
    // ==============================================================================================================

    @Operation(summary = "상품 세트에 속한 상품 조회 API", description = "상품 세트에 속한 상품 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    @FailApiResponses
    @GetMapping("/bundle/{id}")
    public ResponseEntity<ServerResponse<?>> getBundleProducts(@PathVariable("id") Long bundleId,
                                                               Pageable pageable) {

        return ResponseEntity.ok(new ServerResponse<>(adminService.getProductByBundleId(bundleId, pageable)));
    }

    @Operation(summary = "이벤트에 속한 상품 조회 API", description = "이벤트에 속한 상품 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    @FailApiResponses
    @GetMapping("/event/{id}")
    public ResponseEntity<ServerResponse<?>> getEventProducts(@PathVariable("id") Long eventId, Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.getProductByEventId(eventId, pageable)));
    }

    @Operation(summary = "카테고리에 속한 상품 조회 API", description = "카테고리에 속한 상품 조회 API 입니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    @FailApiResponses
    @GetMapping("/category/{id}")
    public ResponseEntity<ServerResponse<?>> getCategoryProducts(@PathVariable("id") Long categoryId,
                                                                 Pageable pageable) {
        return ResponseEntity
            .ok(new ServerResponse<>(adminService.getProductByCategoryId(categoryId, pageable)));
    }

    @Operation(summary = "상품 생성 API", description = "상품 생성 API입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @PostMapping("/product")
    public ResponseEntity<ServerResponse<?>> createProduct(
        @RequestBody ProductDTO.ProductCreateRequest createRequest) throws JsonProcessingException {
        return ResponseEntity.ok(new ServerResponse<>(adminService.createProduct(createRequest)));
    }

    @Operation(summary = "상품 메인 썸네일 이미지 등록 API", description = "상품 메인 썸네일 이미지 등록 API입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ProductImageInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @PostMapping("/product/{id}/mainThumbnail")
    public ResponseEntity<ServerResponse<?>> uploadMainThumbnail(@PathVariable("id") Long id,
                                                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 상품 썸네일 이미지") @RequestParam("mainThumbnail") MultipartFile mainThumbnail) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.uploadMainThumbnail(id, mainThumbnail)));
    }

    @Operation(summary = "상품 서브 썸네일 이미지 등록 API", description = "상품 서브 썸네일 이미지 등록 API입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ProductImageInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @PostMapping("/product/{id}/subThumbnail")
    public ResponseEntity<ServerResponse<?>> uploadSubThumbnail(@PathVariable("id") Long id,
                                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 상품 썸네일 이미지") @RequestParam("subThumbnail") MultipartFile subThumbnail) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.uploadSubThumbnail(id, subThumbnail)));
    }

    @Operation(summary = "상품 메인 이미지들 등록 API", description = "상품 메인 이미지들 등록 API입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ProductImageInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @PostMapping("/product/{id}/mainImages")
    public ResponseEntity<ServerResponse<?>> uploadMainImages(@PathVariable("id") Long id,
                                                              @RequestParam("mainImages") List<MultipartFile> mainImages) throws JsonProcessingException {
        return ResponseEntity.ok(new ServerResponse<>(adminService.uploadMainImages(id, mainImages)));
    }

    @Operation(summary = "상품 상세 이미지들 등록 API", description = "상품 상세 이미지들 등록 API입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ProductImageInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @PostMapping("/product/{id}/detailImages")
    public ResponseEntity<ServerResponse<?>> uploadDetailImages(@PathVariable("id") Long id,
                                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 상품 이미지들") @RequestParam("detailImages") List<MultipartFile> detailImages)
        throws JsonProcessingException {
        return ResponseEntity.ok(new ServerResponse<>(adminService.uploadDetailImages(id, detailImages)));
    }

    @Operation(summary = "상품 정보 업데이트 API", description = "상품 정보 업데이트 API 입니다.")
    @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @PatchMapping("/product/{id}")
    public ResponseEntity<ServerResponse<?>> updateProductInfo(@PathVariable("id") Long id,

                                                               @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "가계부 업데이트 요청", content = @Content(schema = @Schema(implementation = ProductUpdateRequest.class))) @RequestBody Map<String, Object> updateMap)
        throws JsonProcessingException {

        ProductUpdateRequest productUpdateRequest = objectMapper.convertValue(updateMap,
            ProductUpdateRequest.class);
        return ResponseEntity
            .ok(new ServerResponse<>(adminService.updateProductInfo(id, productUpdateRequest)));
    }

    @Operation(summary = "상품 메인 썸네일 이미지 업데이트 API", description = "상품 메인 썸네일 이미지 업데이트 API입니다.")
    @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = ProductImageInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @PutMapping("/product/{id}/mainThumbnail")
    public ResponseEntity<ServerResponse<?>> updateMainThumbnail(@PathVariable("id") Long id,
                                                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "업데이트할 상품 썸네일 이미지") @RequestParam("mainThumbnail") MultipartFile mainThumbnail) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.updateMainThumbnail(id, mainThumbnail)));
    }

    @Operation(summary = "상품 서브 썸네일 이미지 업데이트 API", description = "상품 서브 썸네일 이미지 업데이트 API입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ProductImageInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @PutMapping("/product/{id}/subThumbnail")
    public ResponseEntity<ServerResponse<?>> updateSubThumbnail(@PathVariable("id") Long id,
                                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "업데이트할 상품 썸네일 이미지") @RequestParam("subThumbnail") MultipartFile subThumbnail) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.updateSubThumbnail(id, subThumbnail)));
    }

    @Operation(summary = "상품 메인 이미지들 업데이트 API", description = "상품 메인 이미지들 업데이트 API입니다.")
    @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = ProductImageInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @PutMapping("/product/{id}/mainImages")
    public ResponseEntity<ServerResponse<?>> updateMainImages(@PathVariable("id") Long id,
                                                              @RequestParam("mainImages") List<MultipartFile> mainImages) throws JsonProcessingException {
        return ResponseEntity.ok(new ServerResponse<>(adminService.updateMainImages(id, mainImages)));
    }

    @Operation(summary = "상품 상세 이미지들 업데이트 API", description = "상품 상세 이미지들 업데이트 API입니다.")
    @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = ProductImageInfo.class)))
    @FailApiResponses
    @GetExecutionTime
    @PutMapping("/product/{id}/detailImages")
    public ResponseEntity<ServerResponse<?>> updateDetailImages(@PathVariable("id") Long id,
                                                                @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "업데이트할 상품 이미지들") @RequestParam("detailImages") List<MultipartFile> detailImages)
        throws JsonProcessingException {
        return ResponseEntity.ok(new ServerResponse<>(adminService.updateDetailImages(id, detailImages)));
    }

    @Operation(summary = "상품 삭제 API", description = "상품 삭제 API 입니다.")
    @FailApiResponses
    @GetExecutionTime
    @DeleteMapping("/product/{id}")
    public ResponseEntity<ServerResponse<?>> deleteProduct(@PathVariable("id") Long id)
        throws JsonProcessingException {
        return ResponseEntity.ok(new ServerResponse<>(adminService.deleteProduct(id)));
    }

    // ==============================================================================================================
    // 카테고리 관련 API
    // ==============================================================================================================
    @Operation(summary = "카테고리 생성 API", description = "카테고리 생성 API입니다")
    @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = CategoryInfo.class)))
    @FailApiResponses
    @PostMapping("/category")
    public ResponseEntity<ServerResponse<?>> createCategory(
        @RequestBody CategoryCreateRequest createRequest) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.createCategory(createRequest)));
    }

    @Operation(summary = "카테고리 업데이트 API", description = "카테고리 업데이트 API입니다.")
    @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = CategoryInfo.class)))
    @FailApiResponses
    @PatchMapping("/category/{id}")
    public ResponseEntity<ServerResponse<?>> updateCategory(@PathVariable("id") Long id,
                                                            @RequestBody CategoryUpdateRequest categoryUpdateRequest) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.updateCategory(id, categoryUpdateRequest)));
    }

    @Operation(summary = "카테고리 삭제 API", description = "카테고리 삭제 API 입니다")
    @FailApiResponses
    @DeleteMapping("/category/{id}")
    public ResponseEntity<ServerResponse<?>> deleteCategory(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.deleteCategory(id)));
    }

    // ==============================================================================================================
    // 이벤트 관련 API
    // ==============================================================================================================
    @Operation(summary = "이벤트 생성 API", description = "이벤트 생성 API입니다.")
    @ApiResponse(responseCode = "200", description = "추가 성공")
    @FailApiResponses
    @PostMapping("/event")
    public ResponseEntity<ServerResponse<?>> addBundle(@RequestBody EventCreateRequest createRequest) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.createEvent(createRequest)));
    }

    @Operation(summary = "이벤트 이미지 등록 API", description = "이벤트 이미지 등록 API입니다")
    @ApiResponse(responseCode = "200", description = "추가 성공", content = @Content(schema = @Schema(implementation = EventInfo.class)))
    @FailApiResponses
    @PostMapping("/event/{id}/images")
    public ResponseEntity<ServerResponse<?>> addImagesToEvent(@PathVariable("id") Long id,
                                                              @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 이벤트 이미지들") @RequestParam("images") List<MultipartFile> images) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.uploadEventImages(id, images)));
    }

    @Operation(summary = "이벤트 업데이트 API", description = "이벤트 업데이트 API 입니다.")
    @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = EventUpdateRequest.class)))
    @FailApiResponses
    @PatchMapping("/event/{id}")
    public ResponseEntity<ServerResponse<?>> updateEvent(@PathVariable("id") Long id,
                                                         @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "이벤트 업데이트 요청", content = @Content(schema = @Schema(implementation = EventUpdateRequest.class))) @RequestBody Map<String, Object> updateMap) {
        EventUpdateRequest eventUpdateRequest = objectMapper.convertValue(updateMap, EventUpdateRequest.class);
        return ResponseEntity.ok(new ServerResponse<>(adminService.updateEvent(id, eventUpdateRequest)));
    }

    @Operation(summary = "이벤트 이미지 업데이트 API", description = "이벤트 이미지 업데이트 API입니다.")
    @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = EventInfo.class)))
    @FailApiResponses
    @PutMapping("/event/{id}/images")
    public ResponseEntity<ServerResponse<?>> updateEventImage(@PathVariable("id") Long id,
                                                              @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 이벤트 이미지들") @RequestParam("images") List<MultipartFile> images) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.updateEventImages(id, images)));
    }

    @Operation(summary = "이벤트 삭제", description = "이벤트 삭제 API 입니다")
    @FailApiResponses
    @DeleteMapping("/event/{id}")
    public ResponseEntity<ServerResponse<?>> deleteEvent(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.deleteEvent(id)));
    }

    @Operation(summary = "제품 이벤트 응모 신청 내역 조회 API", description = "해당 제품이 받은 이벤트 응모 신청 내역을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = ProductEventApplyHistoryDTO.class)))
    @FailApiResponses
    @GetMapping("/event/apply/{productId}")
    public ResponseEntity<ServerResponse<?>> getProductEventApplyHistoryDTOs(
        @PathVariable("productId") Long productId,
        Pageable pageable) {
        return ResponseEntity
            .ok(new ServerResponse<>(
                adminService.getProductEventApplyHistoryDTOs(productId, pageable)));
    }

    // ==============================================================================================================
    // 상품 세트 관련 API
    // ==============================================================================================================
    @Operation(summary = "상품 세트 생성 API", description = "상품 세트 생성 API입니다.")
    @ApiResponse(responseCode = "200", description = "생성 성공")
    @FailApiResponses
    @PostMapping("/bundle")
    public ResponseEntity<ServerResponse<?>> addBundle(@RequestBody BundleCreateRequest createRequest) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.createBundle(createRequest)));
    }

    @Operation(summary = "상품 세트 이미지 등록 API", description = "상품 세트 이미지 등록 API입니다")
    @ApiResponse(responseCode = "200", description = "추가 성공", content = @Content(schema = @Schema(implementation = BundleInfo.class)))
    @FailApiResponses
    @PostMapping("/bundle/{id}/images")
    public ResponseEntity<ServerResponse<?>> addImagesToBundle(@PathVariable("id") Long id,
                                                               @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 상품 세트 이미지들") @RequestParam("images") List<MultipartFile> images) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.uploadBundleImages(id, images)));
    }

    @Operation(summary = "상품 세트 업데이트 API", description = "상품 세트 업데이트 API 입니다")
    @ApiResponse(responseCode = "200", description = "업데이트 성공시", content = @Content(schema = @Schema(implementation = BundleUpdateRequest.class)))
    @FailApiResponses
    @PatchMapping("/bundle/{id}")
    public ResponseEntity<ServerResponse<?>> updateBundle(@PathVariable("id") Long id,
                                                          @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "이벤트 업데이트 요청", content = @Content(schema = @Schema(implementation = BundleUpdateRequest.class))) @RequestBody Map<String, Object> updateMap) {
        BundleUpdateRequest bundleUpdateRequest = objectMapper.convertValue(updateMap,
            BundleUpdateRequest.class);
        return ResponseEntity.ok(new ServerResponse<>(adminService.updateBundle(id, bundleUpdateRequest)));
    }

    @Operation(summary = "상품 세트 이미지 업데이트 API", description = "상품 세트 이미지 업데이트 API입니다.")
    @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = BundleInfo.class)))
    @FailApiResponses
    @PutMapping("/bundle/{id}/images")
    public ResponseEntity<ServerResponse<?>> updateBundleImage(@PathVariable("id") Long id,
                                                               @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 상품 세트 이미지들") @RequestParam("images") List<MultipartFile> images) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.updateBundleImages(id, images)));
    }

    @Operation(summary = "상품 세트 삭제 API", description = "상품 세트 삭제 API입니다.")
    @FailApiResponses
    @DeleteMapping("/bundle/{id}")
    public ResponseEntity<ServerResponse<?>> deleteBundle(@PathVariable("id") Long id) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.deleteBundle(id)));
    }

    // ==============================================================================================================
    // 회원 관련 API
    // ==============================================================================================================
    @Operation(summary = "회원 전체 정보 조회 API", description = "홈페이지에 가입된 모든 회원의 정보를 페이지네이션하여 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = MemberInfoDto.class)))
    @FailApiResponses
    @GetMapping("/members")
    public ResponseEntity<ServerResponse<?>> getAllMembers(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.getAllMembers(pageable)));
    }

    @Operation(summary = "이메일로 회원 정보 조회 API", description = "회원 이메일 주소를 이용하여 해당 회원의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = MemberInfoDto.class)))
    @FailApiResponses
    @GetMapping("/member/{email}")
    public ResponseEntity<MemberInfoDto> getMemberByEmail(@PathVariable String email) {
        return ResponseEntity.ok(adminService.getMemberByEmail(email));
    }

    // ==============================================================================================================
    // 주문 관련 API
    // ==============================================================================================================
    @Operation(summary = "괸리자 회원 주문 조회 API", description = "관리자 주문 정보 조회 API")
    @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = GetMemberOrderInfo.class)))
    @FailApiResponses
    @GetMapping("/orders")
    public ResponseEntity<ServerResponse<Page<GetMemberOrderInfo>>> getMemberOrders(
        @Parameter(name = "orderStatus", description = "주문 상태 아무것도 입력안하면 전체 상태 조회되고, 보고싶은 상태만 설정 가능 (CREATED, PENDING, PAID, DELIVERY_STARTED, DELIVERED, COMPLETED, CANCELLED, RETURNED, FAILED )", required = false, in = QUERY)
        @RequestParam(name = "orderStatus", required = false)
        String[] orderStatus,
        @Parameter(name = "receiverName", description = "수취인명", required = false, in = QUERY)
        @RequestParam(name = "receiverName", required = false)
        String receiverName,
        Pageable pageable
    ) {
        GetMemberOrderCondition condition = new GetMemberOrderCondition(orderStatus, receiverName, pageable);
        return ResponseEntity.ok(new ServerResponse<>(adminService.getMemberOrders(condition)));
    }

    @Operation(summary = "송장 번호 설정", description = "송장 번호 설정 API")
    @PatchMapping("/orders/{productOrderNumber}/trackingNumber")
    public ResponseEntity<ServerResponse<String>> updateOrderTrackingNumber(
        @Parameter(name = "productOrderNumber", description = "송장 번호 업데이트할 상품 주문 번호", required = true, in = PATH)
        @PathVariable(name = "productOrderNumber")
        Long productOrderNumber,
        @RequestBody
        UpdateTrackingNumberRequest trackingNumberRequest
    ) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.updateTrackingNumber(productOrderNumber, trackingNumberRequest)));
    }

    // ==============================================================================================================
    // 주문 취소 요청 관련 API
    // ==============================================================================================================

    @Operation(summary = "관리자 주문 취소 요청 조회", description = "주문 취소 요청 조회 API")
    @ApiResponse(responseCode = "200", description = "정보 조회 성공시", content = @Content(schema = @Schema(implementation = GetCancelRequestInfo.class)))
    @GetMapping("/cancelRequest")
    public ResponseEntity<ServerResponse<?>> getCancelRequest(
        @Parameter(name = "orderNumber", description = "주문번호", required = false, in = QUERY)
        @RequestParam(name = "orderNumber", required = false)
        Long orderNumber,
        @Parameter(name = "receiverName", description = "수취인명", required = false, in = QUERY)
        @RequestParam(name = "receiverName", required = false)
        String receiverName,
        @Parameter(name = "cancelRequestStatus", description = "주문 취소 요청 상태, PENDING, DENIED, COMPLETED", required = false, in = QUERY)
        String cancelRequestStatus,
        Pageable pageable) {
        GetCancelRequestCondition cancelRequestCondition = new GetCancelRequestCondition(orderNumber, receiverName, getFromIndex(cancelRequestStatus), pageable);
        return ResponseEntity.ok(new ServerResponse<>(adminService.getCancelRequest(cancelRequestCondition)));
    }

    @Operation(summary = "주문 취소 요청 수락", description = "주문 취소 요청 수락, 주문 취소되는 API")
    @PostMapping("/cancelRequest")
    public ResponseEntity<ServerResponse<?>> cancelOrder(
        @RequestBody
        CancelOrderRequest cancelOrderRequest
    ) {
        return ResponseEntity.ok(new ServerResponse<>(adminService.cancelOrder(cancelOrderRequest)));
    }

    // ==============================================================================================================
    // 결제 관련 API
    // ==============================================================================================================
}
