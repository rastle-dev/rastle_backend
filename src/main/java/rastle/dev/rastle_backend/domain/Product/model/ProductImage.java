package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product_image", catalog = "rastle_db")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_Image_id")
    private Long id;

    @OneToOne(mappedBy = "productImage")
    private ProductBase productBase;

    @OneToMany(mappedBy = "productImage", fetch = FetchType.LAZY)
    private Image image;
}
