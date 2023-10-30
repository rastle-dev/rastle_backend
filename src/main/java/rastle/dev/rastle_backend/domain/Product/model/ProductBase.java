package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Bundle.model.Bundle;
import rastle.dev.rastle_backend.domain.Category.model.Category;
import rastle.dev.rastle_backend.domain.Event.model.Event;
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
    @Column(name = "display_order")
    private Long displayOrder;
    private String name;
    private int price;
    @Column(name = "discount_price")
    private int discountPrice;

    @Column(name = "main_thumbnail_image")
    private String mainThumbnailImage;

    @Column(name = "sub_thumbnail_image")
    private String subThumbnailImage;
//    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
//    private List<Color> colors = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CartProduct> cartProduct = new ArrayList<>();
    private boolean visible;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

//    @OneToOne(cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "detail_image_id")
//    private ProductImage detailImage;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "bundle_id")
    private Bundle bundle;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Builder
    public ProductBase(String name, int price, String mainThumbnailImage, String subThumbnailImage, int discountPrice, Category category, Long displayOrder, boolean visible, Bundle bundle, Event event) {
        this.name = name;
        this.price = price;
        this.mainThumbnailImage = mainThumbnailImage;
        this.subThumbnailImage = subThumbnailImage;
        this.discountPrice = discountPrice;
        this.category = category;
        this.displayOrder = displayOrder;
        this.visible = visible;
        this.bundle = bundle;
        this.event = event;
    }

    public void setMainThumbnailImage(String mainThumbnailImage) {
        this.mainThumbnailImage = mainThumbnailImage;
    }

    public void setSubThumbnailImage(String subThumbnailImage) {
        this.subThumbnailImage = subThumbnailImage;
    }

    public void setProductDetail(ProductDetail mainImage) {
        this.productDetail = mainImage;
    }

//    public void setDetailImage(ProductImage detailImage) {
//        this.detailImage = detailImage;
//    }

    public void setDisplayOrder(Long displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDiscountPrice(int discount) {
        this.discountPrice = discount;
    }


//    public void setColors(List<Color> colors) {
//        this.colors = colors;
//    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
