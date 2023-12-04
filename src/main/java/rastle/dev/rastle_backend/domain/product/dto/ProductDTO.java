package rastle.dev.rastle_backend.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.bundle.model.Bundle;
import rastle.dev.rastle_backend.domain.category.model.Category;
import rastle.dev.rastle_backend.domain.event.model.Event;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.domain.product.model.ProductColor;
import rastle.dev.rastle_backend.domain.product.model.ProductImage;

import java.util.List;

public class ProductDTO {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 업데이트 요청 dto")
    public static class ProductUpdateRequest {
        @Schema(description = "상품 이름", defaultValue = "멋있는 자켓")
        String name;
        @Schema(description = "가격", defaultValue = "100000")
        Integer price;
        @Schema(description = "할인가격", defaultValue = "90000")
        Integer discountPrice;
        @Schema(description = "카테고리 아이디", defaultValue = "2")
        Long categoryId;
        @Schema(description = "제품 색상, 사이즈 정세", defaultValue = "list of colorinfo")
        ProductColor productColor;
        @Schema(description = "상품 보여질 순서", defaultValue = "1000")
        Long displayOrder;
        @Schema(description = "상품 보여질지 여부", defaultValue = "true")
        Boolean visible;
        @Schema(description = "이벤트 아이디", defaultValue = "1")
        Long eventId;
        @Schema(description = "번들 아이디", defaultValue = "1")
        Long bundleId;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 업데이트 결과 dto")
    public static class ProductUpdateResult {
        @Schema(description = "상품 이름", defaultValue = "멋있는 자켓")
        String name;
        @Schema(description = "가격", defaultValue = "100000")
        int price;
        @Schema(description = "할인가격", defaultValue = "90000")
        int discountPrice;
        @Schema(description = "이벤트 혹은 세트아이디", defaultValue = "1")
        Long marketOrBundleId;
        @Schema(description = "카테고리 아이디", defaultValue = "2")
        Long categoryId;
        @Schema(description = "제품 색상, 사이즈 정세", defaultValue = "list of colorinfo")
        ProductColor productColor;
        @Schema(description = "상품 보여질 순서", defaultValue = "1000")
        Long displayOrder;
        @Schema(description = "상품 보여질지 여부", defaultValue = "true")
        Boolean visible;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 생성 요청 dto")
    public static class ProductCreateRequest {
        @Schema(description = "상품 이름", defaultValue = "멋있는 자켓")
        String name;
        @Schema(description = "가격", defaultValue = "100000")
        int price;
        @Schema(description = "할인가격", defaultValue = "90000")
        int discountPrice;
        @Schema(description = "카테고리 아이디", defaultValue = "2")
        Long categoryId;
        @Schema(description = "제품 색상, 사이즈 정세", defaultValue = "list of colorinfo")
        ProductColor productColor;
        @Schema(description = "상품 보여질 순서", defaultValue = "1000")
        Long displayOrder;
        @Schema(description = "상품 보여질지 여부", defaultValue = "true")
        boolean visible;
        @Schema(description = "번들아이디", defaultValue = "1")
        Long bundleId;
        @Schema(description = "이벤트아이디", defaultValue = "1")
        Long eventId;


        public ProductBase toProductBase(Category category, Bundle bundle, Event event) {
            return new ProductBase(name, price,  null, null, discountPrice, category, displayOrder, visible, bundle, event);
        }

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "상품 상세 이미지 dto")
    public static class ProductImages {
        @Schema(description = "상품 메인 이미지 리스트")
        List<String> mainImages;
        @Schema(description = "상품 상세 이미지 리스트")
        List<String> detailImages;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "상품 생성 결과")
    public static class ProductCreateResult {
        @Schema(description = "상품 아이디", defaultValue = "1")
        Long id;
        @Schema(description = "상품 이름", defaultValue = "멋있는 자켓")
        String name;
        @Schema(description = "상품 가격", defaultValue = "100000")
        int price;
        @Schema(description = "상품 할인가격", defaultValue = "90000")
        int discountPrice;
        @Schema(description = "이벤트 상품이면 이벤트 아이디, 마켓 상품이면 마켓 아이디", defaultValue = "2")
        Long categoryId;
        @Schema(description = "색상 & 사이즈 정보", defaultValue = "상품 색상 & 사이즈 정보")
        ProductColor productColor;
        @Schema(description = "상품 보여질 순서", defaultValue = "100")
        Long displayOrder;
        @Schema(description = "상품 보여질지 여부", defaultValue = "true")
        boolean visible;
        @Schema(description = "번들 아이디", defaultValue = "1")
        Long bundleId;
        @Schema(description = "이벤트 아이디", defaultValue = "1")
        Long eventId;

        public void setBundleId(Long bundleId) {
            this.bundleId = bundleId;
        }

        public void setEventId(Long eventId) {
            this.eventId = eventId;
        }
    }

}
