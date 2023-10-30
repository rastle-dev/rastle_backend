package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_detail", catalog = "rastle_db")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_detail_id")
    private Long id;
    @Column(name = "product_main_images")
    private String productMainImages;
    @Column(name = "product_detail_images")
    private String productDetailImages;
    @Column(name = "product_colors")
    private String productColors;
    @Builder
    public ProductDetail(String productMainImages, String productDetailImages, String productColors) {
        this.productMainImages = productMainImages;
        this.productDetailImages = productDetailImages;
        this.productColors = productColors;
    }
    //    @OneToMany(mappedBy = "productImage", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private List<Image> images = new ArrayList<>();
}
