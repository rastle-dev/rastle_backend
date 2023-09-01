package rastle.dev.rastle_backend.domain.Product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ProductDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCreateRequest {
        String name;
        int price;
        int discount;
        boolean eventCategory;
        Long categoryId;
        List<ColorInfo> colorAndSizes;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductImages {

        List<String> mainImages;
        List<String> detailImages;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductCreateResult {
        Long id;
        String name;
        int price;
        int discount;
        boolean isEvent;
        Long categoryId;
        List<ColorInfo> colorAndSizes;
    }


}
