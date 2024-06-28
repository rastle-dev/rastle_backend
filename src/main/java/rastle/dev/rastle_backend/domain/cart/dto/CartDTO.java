package rastle.dev.rastle_backend.domain.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CartDTO {
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "장바구니 제품 추가 DTO")
    public static class CreateCartProductDto {
        @Schema(description = "제품 ID", type = "long", format = "id", example = "1")
        private Long productId;
        @Schema(description = "선택 제품 색상", type = "string", format = "color", example = "베이지")
        private String color;
        @Schema(description = "선택 제품 사이즈", type = "string", format = "size", example = "L")
        private String size;
        @Schema(description = "선택 제품 수량", type = "int", format = "count", example = "1")
        private int count;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "장바구니 제품 정보 DTO")
    public static class CartProductInfoDto {
        @Schema(description = "장바구니 제품 ID", type = "long", format = "id", example = "1")
        private Long cartProductId;
        @Schema(description = "장바구니 제품 이름", type = "string", format = "name", example = "청바지")
        private String productName;
        @Schema(description = "장바구니 제품 가격", type = "int", format = "price", example = "10000")
        private int productPrice;
        @Schema(description = "장바구니 제품 할인 가격", type = "int", format = "discountPrice", example = "8000")
        private int discountPrice;
        @Schema(description = "장바구니 제품 색상", type = "string", format = "color", example = "베이지")
        private String color;
        @Schema(description = "장바구니 제품 사이즈", type = "string", format = "size", example = "L")
        private String size;
        @Schema(description = "장바구니 제품 수량", type = "int", format = "count", example = "1")
        private int count;
        @Schema(description = "제품 메인 썸네일 이미지", type = "string", format = "image", example = "image-url")
        private String mainThumbnailImage;
        @Schema(description = "제품 ID", type = "long", format = "id", example = "1")
        private Long productId;
        @Schema(description = "품절 여부", type = "boolean")
        private Boolean soldOut;
    }
}
