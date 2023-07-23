package rastle.dev.rastle_backend.domain.Market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "color", catalog = "rastle_db")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private MarketProduct marketProduct;

    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Size> sizes = new ArrayList<>();
}
