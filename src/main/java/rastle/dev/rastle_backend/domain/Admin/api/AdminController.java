package rastle.dev.rastle_backend.domain.Admin.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import rastle.dev.rastle_backend.domain.Product.dto.ProductDTO;
import rastle.dev.rastle_backend.domain.Product.dto.ProductImageInfo;
import rastle.dev.rastle_backend.domain.Product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.global.common.annotation.GetExecutionTime;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;
import rastle.dev.rastle_backend.global.response.CustomApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import java.io.IOException;
import java.util.List;

@Tag(name = "관리자 기능 API", description = "관리자 기능 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
        private final AdminService adminService;

        @Operation(summary = "상품 생성 API", description = "상품 생성 API 입니다.")
        // @ApiResponses(value = {
        // @ApiResponse(responseCode = "200", description = "생성 성공시", content =
        // @Content(schema = @Schema(implementation = SimpleProductInfo.class))),
        // @ApiResponse(responseCode = "401", description = "토큰 만료시", content =
        // @Content(schema = @Schema(implementation = ErrorResponse.class))),
        // @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시",
        // content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        // @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시",
        // content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        // })
        @CustomApiResponses(implementation = SimpleProductInfo.class)
        @GetExecutionTime
        @PostMapping("/product")
        public ResponseEntity<ServerResponse<?>> createProduct(
                        @RequestBody ProductDTO.ProductCreateRequest createRequest) throws IOException {

                return ResponseEntity.ok(new ServerResponse<>(adminService.createProduct(createRequest)));
        }

        @Operation(summary = "상품 메인 썸네일 이미지 등록 API", description = "상품 메인 썸네일 이미지 등록 API 입니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = ProductImageInfo.class))),
                        @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetExecutionTime
        @PostMapping("/product/{id}/mainThumbnail")
        public ResponseEntity<ServerResponse<?>> uploadMainThumbnail(@PathVariable("id") Long id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 상품 썸네일 이미지") @RequestParam("mainThumbnail") MultipartFile mainThumbnail) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.uploadMainThumbnail(id, mainThumbnail)));

        }

        @Operation(summary = "상품 서브 썸네일 이미지 등록 API", description = "상품 서브 썸네일 이미지 등록 API 입니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = ProductImageInfo.class))),
                        @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetExecutionTime
        @PostMapping("/product/{id}/subThumbnail")
        public ResponseEntity<ServerResponse<?>> uploadSubThumbnail(@PathVariable("id") Long id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 상품 썸네일 이미지") @RequestParam("subThumbnail") MultipartFile subThumbnail) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.uploadSubThumbnail(id, subThumbnail)));

        }

        @Operation(summary = "상품 메인 이미지들 등록 API", description = "상품 메인 이미지들 등록 API 입니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = ProductImageInfo.class))),
                        @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetExecutionTime
        @PostMapping("/product/{id}/mainImages")
        public ResponseEntity<ServerResponse<?>> uploadMainImages(@PathVariable("id") Long id,

                        @RequestParam("mainImages") List<MultipartFile> mainImages) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.uploadMainImages(id, mainImages)));

        }

        @Operation(summary = "상품 상세 이미지들 등록 API", description = "상품 상세 이미지들 등록 API 입니다.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = ProductImageInfo.class))),
                        @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @GetExecutionTime
        @PostMapping("/product/{id}/detailImages")
        public ResponseEntity<ServerResponse<?>> uploadDetailImages(@PathVariable("id") Long id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 상품 이미지들") @RequestParam("detailImages") List<MultipartFile> detailImages) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.uploadDetailImages(id, detailImages)));

        }

        @Operation(summary = "카테고리 생성 API", description = "카테고리 생성 API입니다")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = CategoryInfo.class))),
                        @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/category")
        public ResponseEntity<ServerResponse<?>> createCategory(
                        @RequestBody CategoryDto.CategoryCreateRequest createRequest) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.createCategory(createRequest)));
        }

        @Operation(summary = "카테고리 이미지 추가 API", description = "카테고리 이미지 추가 API입니다")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = CategoryInfo.class))),
                        @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/category/{id}/images")
        public ResponseEntity<ServerResponse<?>> addImagesToCategory(@PathVariable("id") Long id,
                        @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "등록할 카테고리 이미지들") @RequestParam("images") List<MultipartFile> images) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.uploadImages(id, images)));
        }

        @Operation(summary = "이벤트 생성 API", description = "이벤트 생성 API입니다")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "추가 성공시"),
                        @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/event")
        public ResponseEntity<ServerResponse<?>> tempAddMarket(@RequestBody EventCreateRequest createRequest) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.createEvent(createRequest)));
        }

        @Operation(summary = "마켓 생성 API", description = "마켓 생성 API입니다")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "생성 성공시"),
                        @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        @PostMapping("/market")
        public ResponseEntity<ServerResponse<?>> tempAddMarket(@RequestBody MarketCreateRequest createRequest) {
                return ResponseEntity.ok(new ServerResponse<>(adminService.createMarket(createRequest)));
        }

}
