package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "product_image", catalog = "rastle_db")
public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Long id;
    private String mainImageUrls;
    private String detailImageUrls;

//    @OneToMany(mappedBy = "productImage", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private List<Image> images = new ArrayList<>();
}
