package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product_detail", catalog = "rastle_db")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_detail_id")
    private Long id;
    private String productMainImages;
    private String productDetailImages;
    private String productColors;

//    @OneToMany(mappedBy = "productImage", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private List<Image> images = new ArrayList<>();
}
