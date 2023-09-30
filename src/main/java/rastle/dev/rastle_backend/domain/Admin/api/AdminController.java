package rastle.dev.rastle_backend.domain.Admin.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.Admin.application.AdminService;
import rastle.dev.rastle_backend.domain.Bundle.dto.BundleDTO.BundleUpdateRequest;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto.CategoryCreateRequest;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto.CategoryUpdateRequest;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryInfo;
import rastle.dev.rastle_backend.domain.Event.dto.EventDTO.EventCreateRequest;
import rastle.dev.rastle_backend.domain.Event.dto.EventDTO.EventUpdateRequest;
import rastle.dev.rastle_backend.domain.Event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.Bundle.dto.BundleDTO.BundleCreateRequest;
import rastle.dev.rastle_backend.domain.Bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.MemberInfoDto;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO.ProductUpdateRequest;
import rastle.dev.rastle_backend.domain.Product.dto.ProductImageInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.common.annotation.GetExecutionTime;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import java.util.List;
import java.util.Map;

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
        @Operation(summary = "상품 생성 API", description = "상품 생성 API입니다.")
        @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
        @FailApiResponses
        @GetExecutionTime
        @PostMapping("/product")
        public ResponseEntity<ServerResponse<?>> createProduct(
                        @RequestBody ProductDTO.ProductCreateRequest createRequest) {
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
                        @RequestParam("mainImages") List<MultipartFile> mainImages) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.uploadMainImages(id, mainImages)));
        }

        @Operation(summary = "상품 상세 이미지들 등록 API", description = "상품 상세 이미지들 등록 API입니다.")
        @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = ProductImageInfo.class)))
        @FailApiResponses
        @GetExecutionTime
        @PostMapping("/product/{id}/detailImages")
        public ResponseEntity<ServerResponse<?>> uploadDetailImages(@PathVariable("id") Long id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 상품 이미지들") @RequestParam("detailImages") List<MultipartFile> detailImages) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.uploadDetailImages(id, detailImages)));
        }

        @Operation(summary = "상품 정보 업데이트 API", description = "상품 정보 업데이트 API 입니다.")
        @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
        @FailApiResponses
        @GetExecutionTime
        @PatchMapping("/product/{id}")
        public ResponseEntity<ServerResponse<?>> updateProductInfo(@PathVariable("id") Long id,
                                                                   @Parameter(name = "isEvent", description = "true or false로 무조건 보내줘야함, 이벤트 상품이면 true", required = true, example = "true")
                                                                   @RequestParam(name = "isEvent") Boolean isEvent,
                                                                   @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "가계부 업데이트 요청", content = @Content(schema = @Schema(implementation = ProductUpdateRequest.class)))
                                                                   @RequestBody Map<String, Object> updateMap) {

                ProductUpdateRequest productUpdateRequest = objectMapper.convertValue(updateMap, ProductUpdateRequest.class);
                return ResponseEntity.ok(new ServerResponse<>(adminService.updateProductInfo(id, productUpdateRequest, isEvent)));
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
                                                                  @RequestParam("mainImages") List<MultipartFile> mainImages) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.updateMainImages(id, mainImages)));
        }

        @Operation(summary = "상품 상세 이미지들 업데이트 API", description = "상품 상세 이미지들 업데이트 API입니다.")
        @ApiResponse(responseCode = "200", description = "업데이트 성공", content = @Content(schema = @Schema(implementation = ProductImageInfo.class)))
        @FailApiResponses
        @GetExecutionTime
        @PutMapping("/product/{id}/detailImages")
        public ResponseEntity<ServerResponse<?>> updateDetailImages(@PathVariable("id") Long id,
                                                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "업데이트할 상품 이미지들") @RequestParam("detailImages") List<MultipartFile> detailImages) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.updateDetailImages(id, detailImages)));
        }



        @Operation(summary = "상품 삭제 API", description = "상품 삭제 API 입니다.")
        @FailApiResponses
        @GetExecutionTime
        @DeleteMapping("/product/{id}")
        public ResponseEntity<ServerResponse<?>> deleteProduct(@PathVariable("id") Long id) {
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
        public ResponseEntity<ServerResponse<?>> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryUpdateRequest categoryUpdateRequest) {
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
        public ResponseEntity<ServerResponse<?>> updateEvent(@PathVariable("id") Long id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "이벤트 업데이트 요청", content = @Content(schema = @Schema(implementation = EventUpdateRequest.class))) @RequestBody Map<String, Object> updateMap) {
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
                                                              @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "이벤트 업데이트 요청", content = @Content(schema = @Schema(implementation = BundleUpdateRequest.class)))
                                                              @RequestBody Map<String, Object> updateMap) {
                BundleUpdateRequest bundleUpdateRequest = objectMapper.convertValue(updateMap, BundleUpdateRequest.class);
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
}
