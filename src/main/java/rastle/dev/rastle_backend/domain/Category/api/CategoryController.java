package rastle.dev.rastle_backend.domain.Category.api;

import com.amazonaws.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rastle.dev.rastle_backend.domain.Category.application.CategoryService;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryDto.CategoryCreateRequest;
import rastle.dev.rastle_backend.domain.Category.dto.CategoryInfo;
import rastle.dev.rastle_backend.global.error.response.ErrorResponse;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import java.util.List;

@Tag(name = "카테고리 관련 API", description = "카테고리 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    @Operation(summary = "카테고리 생성 API", description = "카테고리 생성 API입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "생성 성공시", content = @Content(schema = @Schema(implementation = CategoryInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("")
    public ResponseEntity<ServerResponse<?>> createCategory(@RequestBody CategoryCreateRequest createRequest) {
        return ResponseEntity.ok(new ServerResponse<>(categoryService.createCategory(createRequest)));
    }
    @Operation(summary = "카테고리 이미지 추가 API", description = "카테고리 이미지 추가 API입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "추가 성공시", content = @Content(schema = @Schema(implementation = CategoryInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/{id}/images")
    public ResponseEntity<ServerResponse<?>> addImagesToCategory(@PathVariable("id") Long id, @Parameter(description = "등록할 카테고리 이미지들") @RequestParam("images") List<MultipartFile> images) {
        return ResponseEntity.ok(new ServerResponse<>(categoryService.uploadImages(id, images)));
    }
    @Operation(summary = "카테고리 이미지 조회 API", description = "카테고리 이미지 조회 API입니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공시", content = @Content(schema = @Schema(implementation = CategoryInfo.class))),
            @ApiResponse(responseCode = "401", description = "토큰 만료시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "클라이언트에서 잘못된 데이터 전송시", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "서버 내부에서 핸들링되지 않은 예외 발생시", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getCategories() {
        return ResponseEntity.ok(new ServerResponse<>(categoryService.getCategories()));
    }
}

