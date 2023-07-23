package rastle.dev.rastle_backend.domain.Market.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "sale_end_time")
    private LocalDateTime saleEndTime;

    @OneToMany(mappedBy = "market", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<MarketProduct> marketProducts = new ArrayList<>();
}
