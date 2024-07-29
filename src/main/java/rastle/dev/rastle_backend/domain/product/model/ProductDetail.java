package rastle.dev.rastle_backend.domain.product.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Cache(usage = READ_WRITE)
@Cacheable
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_detail")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_detail_id")
    private Long id;
    @Convert(converter = ProductImage.ProductImageConverter.class)
    @Column(name = "product_main_images", columnDefinition = "JSON")
    private ProductImage productMainImages;
    @Convert(converter = ProductImage.ProductImageConverter.class)
    @Column(name = "product_detail_images", columnDefinition = "JSON")
    private ProductImage productDetailImages;
    @Convert(converter = ProductColor.ProductColorConverter.class)
    @Column(name = "product_colors", columnDefinition = "JSON")
    private ProductColor productColors;

    @Builder
    public ProductDetail(ProductImage productMainImages, ProductImage productDetailImages, ProductColor productColors) {
        this.productMainImages = productMainImages;
        this.productDetailImages = productDetailImages;
        this.productColors = productColors;
    }
}
