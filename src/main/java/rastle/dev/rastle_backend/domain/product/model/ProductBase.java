package rastle.dev.rastle_backend.domain.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.bundle.model.Bundle;
import rastle.dev.rastle_backend.domain.category.model.Category;
import rastle.dev.rastle_backend.domain.event.model.Event;
import rastle.dev.rastle_backend.domain.event.model.EventProductApply;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_base")
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

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CartProduct> cartProduct = new ArrayList<>();
    private boolean visible;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;

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

    @OneToMany(mappedBy = "eventApplyProduct", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<EventProductApply> eventProductApplies = new ArrayList<>();

    @Builder
    public ProductBase(Long id, String name, int price, String mainThumbnailImage, String subThumbnailImage,
            int discountPrice, Category category, Long displayOrder, boolean visible, Bundle bundle, Event event) {
        this.id = id;
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
