package rastle.dev.rastle_backend.domain.Product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Schema(description = "전체 상품 조회 시 리턴 데이터")
public class SimpleBundleOrEventProductInfo extends SimpleProductInfo {
    public SimpleBundleOrEventProductInfo(Long id, String name, int price, String mainThumbnail, String subThumbnail, boolean isEvent, int discountPrice, Long displayOrder, boolean visible, Long categoryId, Long eventOrBundleId) {
        super(id, name, price, mainThumbnail, subThumbnail, isEvent, discountPrice, displayOrder, visible, categoryId);
        this.eventOrBundleId = eventOrBundleId;
    }

    @Schema(description = "번들 혹은 이벤트 아이디", defaultValue = "2")
    Long eventOrBundleId;
}
