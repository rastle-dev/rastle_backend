package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Category.model.Category;
import rastle.dev.rastle_backend.domain.Orders.model.OrderProduct;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_base", catalog = "rastle_db")
@Inheritance(strategy = InheritanceType.JOINED)
public class ProductBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    private int price;
    private int discount;
    @Column(name = "is_event_product")
    private boolean isEventProduct;

    @Column(name = "main_thumbnail_image")
    private String mainThumbnailImage;

    @Column(name = "sub_thumbnail_image")
    private String subThumbnailImage;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Color> colors = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CartProduct> cartProduct = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "main_image_id")
    private ProductImage mainImage;

    @OneToOne
    @JoinColumn(name = "detail_image_id")
    private ProductImage detailImage;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    public ProductBase(String name, int price, boolean isEventProduct, String mainThumbnailImage, String subThumbnailImage, int discount, Category category) {
        this.name = name;
        this.price = price;
        this.isEventProduct = isEventProduct;
        this.mainThumbnailImage = mainThumbnailImage;
        this.subThumbnailImage = subThumbnailImage;
        this.discount = discount;
        this.category = category;
    }

    public void setMainThumbnailImage(String mainThumbnailImage) {
        this.mainThumbnailImage = mainThumbnailImage;
    }

    public void setSubThumbnailImage(String subThumbnailImage) {
        this.subThumbnailImage = subThumbnailImage;
    }

    public void setMainImage(ProductImage mainImage) {
        this.mainImage = mainImage;
    }

    public void setDetailImage(ProductImage detailImage) {
        this.detailImage = detailImage;
    }
}
