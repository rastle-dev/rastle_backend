package rastle.dev.rastle_backend.domain.Market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
}
