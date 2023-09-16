package rastle.dev.rastle_backend.domain.Admin.api;

import io.swagger.v3.oas.annotations.Operation;
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
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryInfo;
import rastle.dev.rastle_backend.domain.Event.dto.EventDTO.EventCreateRequest;
import rastle.dev.rastle_backend.domain.Market.dto.MarketDTO.MarketCreateRequest;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.MemberInfoDto;
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO;
import rastle.dev.rastle_backend.domain.Product.dto.ProductImageInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.common.annotation.GetExecutionTime;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import java.io.IOException;
import java.util.List;

@Tag(name = "관리자 기능 API", description = "관리자 기능 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
        private final AdminService adminService;

        // ==============================================================================================================
        // 상품 관련 API
        // ==============================================================================================================
        @Operation(summary = "상품 생성 API", description = "상품 생성 API입니다.")
        @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = SimpleProductInfo.class)))
        @FailApiResponses
        @GetExecutionTime
        @PostMapping("/product")
        public ResponseEntity<ServerResponse<?>> createProduct(
                        @RequestBody ProductDTO.ProductCreateRequest createRequest) throws IOException {
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

        // ==============================================================================================================
        // 카테고리 관련 API
        // ==============================================================================================================
        @Operation(summary = "카테고리 생성 API", description = "카테고리 생성 API입니다")
        @ApiResponse(responseCode = "200", description = "생성 성공", content = @Content(schema = @Schema(implementation = CategoryInfo.class)))
        @FailApiResponses
        @PostMapping("/category")
        public ResponseEntity<ServerResponse<?>> createCategory(
                        @RequestBody CategoryDto.CategoryCreateRequest createRequest) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.createCategory(createRequest)));
        }

        @Operation(summary = "카테고리 이미지 추가 API", description = "카테고리 이미지 추가 API입니다")
        @ApiResponse(responseCode = "200", description = "추가 성공", content = @Content(schema = @Schema(implementation = CategoryInfo.class)))
        @FailApiResponses
        @PostMapping("/category/{id}/images")
        public ResponseEntity<ServerResponse<?>> addImagesToCategory(@PathVariable("id") Long id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 카테고리 이미지들") @RequestParam("images") List<MultipartFile> images) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.uploadImages(id, images)));
        }

        // ==============================================================================================================
        // 이벤트 관련 API
        // ==============================================================================================================
        @Operation(summary = "이벤트 생성 API", description = "이벤트 생성 API입니다.")
        @ApiResponse(responseCode = "200", description = "추가 성공")
        @FailApiResponses
        @PostMapping("/event")
        public ResponseEntity<ServerResponse<?>> tempAddMarket(@RequestBody EventCreateRequest createRequest) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.createEvent(createRequest)));
        }

        // ==============================================================================================================
        // 마켓 관련 API
        // ==============================================================================================================
        @Operation(summary = "마켓 생성 API", description = "마켓 생성 API입니다.")
        @ApiResponse(responseCode = "200", description = "생성 성공")
        @FailApiResponses
        @PostMapping("/market")
        public ResponseEntity<ServerResponse<?>> tempAddMarket(@RequestBody MarketCreateRequest createRequest) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.createMarket(createRequest)));
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
