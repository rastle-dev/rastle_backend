package rastle.dev.rastle_backend.domain.Market.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Product.model.MarketProduct;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "market", catalog = "rastle_db")
public class Market {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "market_id")
    private Long id;
    private String name;
    @Column(name = "sale_start_time")
    private LocalDateTime saleStartTime;
    @Column(name = "image_urls")
    private String imageUrls;
    private String description;


    @OneToMany(mappedBy = "market", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MarketProduct> marketProducts = new ArrayList<>();
    @Builder
    public Market(String name, LocalDateTime saleStartTime, String imageUrls, String description) {
        this.name = name;
        this.saleStartTime = saleStartTime;
        this.imageUrls = imageUrls;
        this.description = description;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }
}
