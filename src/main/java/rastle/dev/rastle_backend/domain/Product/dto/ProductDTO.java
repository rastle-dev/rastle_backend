package rastle.dev.rastle_backend.domain.Product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Event.model.Event;
import rastle.dev.rastle_backend.domain.Market.model.Market;
import rastle.dev.rastle_backend.domain.Product.model.EventProduct;
import rastle.dev.rastle_backend.domain.Product.model.MarketProduct;

import java.util.List;

public class ProductDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 생성 요청 dto")
    public static class ProductCreateRequest {
        @Schema(description = "상품 이름")
        String name;
        @Schema(description = "가격")
        int price;
        @Schema(description = "할인률")
        int discount;
        @Schema(description = "이벤트 인지 마켓인지")
        boolean eventCategory;
        @Schema(description = "이벤트 혹은 마켓아이디")
        Long categoryId;
        @Schema(description = "제품 색상, 사이즈 정세")
        List<ColorInfo> colorAndSizes;

        public EventProduct toEventProduct(Event event) {
            return EventProduct.builder()
                    .name(name)
                    .price(price)
                    .isEventProduct(true)
                    .discount(discount)
                    .event(event)
                    .build();
        }

        public MarketProduct toMarketProduct(Market market) {
            return MarketProduct.builder()
                    .name(name)
                    .price(price)
                    .isEventProduct(false)
                    .discount(discount)
                    .market(market)
                    .build();
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
        @Schema(description = "상품 아이디")
        Long id;
        @Schema(description = "상품 이름")
        String name;
        @Schema(description = "상품 가격")
        int price;
        @Schema(description = "상품 할인률")
        int discount;
        @Schema(description = "이벤트 상품 여부")
        boolean isEvent;
        @Schema(description = "이벤트 상품이면 이벤트 아이디, 마켓 상품이면 마켓 아이디")
        Long categoryId;
        @Schema(description = "색상 & 사이즈 정보")
        List<ColorInfo> colorAndSizes;
    }

}
