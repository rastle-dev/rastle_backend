package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Category.model.Category;
import rastle.dev.rastle_backend.domain.Market.model.Market;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "market_product", catalog = "rastle_db")
public class MarketProduct extends ProductBase {
    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

    @Builder
    public MarketProduct(String name, int price, boolean isEventProduct, String mainThumbnailImage,
            String subThumbnailImage, Market market, int discount, Category category, Long displayOrder, boolean visible) {
        super(name, price, isEventProduct, mainThumbnailImage, subThumbnailImage, discount, category, displayOrder, visible);
        this.market = market;
    }
}
