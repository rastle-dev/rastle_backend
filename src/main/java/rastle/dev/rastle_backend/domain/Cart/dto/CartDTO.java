package rastle.dev.rastle_backend.domain.Cart.dto;

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
    @Schema(description = "장바구니 아이템 추가 DTO")
    public static class CreateCartItemDto {
        @Schema(description = "장바구니 아이템 ID", type = "long", format = "id", example = "1")
        private Long productId;
        @Schema(description = "장바구니 아이템 색상", type = "string", format = "color", example = "베이지")
        private String color;
        @Schema(description = "장바구니 아이템 사이즈", type = "string", format = "size", example = "L")
        private String size;
        @Schema(description = "장바구니 아이템 수량", type = "int", format = "count", example = "1")
        private int count;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "장바구니 아이템 정보 DTO")
    public static class CartItemInfoDto {
        @Schema(description = "장바구니 아이템 이름", type = "string", format = "name", example = "청바지")
        private String productName;
        @Schema(description = "장바구니 아이템 가격", type = "int", format = "price", example = "10000")
        private int productPrice;
        @Schema(description = "장바구니 아이템 색상", type = "string", format = "color", example = "베이지")
        private String color;
        @Schema(description = "장바구니 아이템 사이즈", type = "string", format = "size", example = "L")
        private String size;
        @Schema(description = "장바구니 아이템 수량", type = "int", format = "count", example = "1")
        private int count;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Schema(description = "장바구니 아이템 삭제 DTO")
    public static class DeleteCartItemDto {
        @Schema(description = "장바구니 아이템 ID", type = "long", format = "id", example = "1")
        private Long productId;
        @Schema(description = "장바구니 아이템 색상", type = "string", format = "color", example = "베이지")
        private String color;
        @Schema(description = "장바구니 아이템 사이즈", type = "string", format = "size", example = "L")
        private String size;
    }
}
