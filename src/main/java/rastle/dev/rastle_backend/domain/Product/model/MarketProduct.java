package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Market.model.Market;
import rastle.dev.rastle_backend.domain.Orders.model.OrderProduct;
import rastle.dev.rastle_backend.domain.Product.model.Color;
import rastle.dev.rastle_backend.domain.Product.model.ProductBase;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "market_product", catalog = "rastle_db")
public class MarketProduct extends ProductBase {
    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;



    @OneToMany(mappedBy = "marketProduct", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Color> colors = new ArrayList<>();
    @Builder
    public MarketProduct(String name, int price, boolean isEventProduct, String mainThumbnailImage, String subThumbnailImage, Market market) {
        super(name, price, isEventProduct, mainThumbnailImage, subThumbnailImage);
        this.market = market;
    }
}
