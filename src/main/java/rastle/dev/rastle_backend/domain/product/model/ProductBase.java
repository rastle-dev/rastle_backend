package rastle.dev.rastle_backend.domain.product.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import rastle.dev.rastle_backend.domain.bundle.model.Bundle;
import rastle.dev.rastle_backend.domain.cart.model.CartProduct;
import rastle.dev.rastle_backend.domain.category.model.Category;
import rastle.dev.rastle_backend.domain.event.model.Event;
import rastle.dev.rastle_backend.domain.event.model.EventProductApply;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

@Cache(usage = NONSTRICT_READ_WRITE)
@Cacheable
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
    @Setter
    @Column(name = "display_order")
    private Long displayOrder;
    @Setter
    private String name;
    @Setter
    private int price;
    @Setter
    @Column(name = "discount_price")
    private int discountPrice;
    @Setter
    @Column(name = "main_thumbnail_image")
    private String mainThumbnailImage;
    @Setter
    @Column(name = "sub_thumbnail_image")
    private String subThumbnailImage;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<OrderProduct> orderProducts = new ArrayList<>();
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CartProduct> cartProduct = new ArrayList<>();
    @Setter
    private boolean visible;

    @Cache(usage = NONSTRICT_READ_WRITE)
    @Setter
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "product_detail_id")
    private ProductDetail productDetail;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @Cache(usage = NONSTRICT_READ_WRITE)
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_id")
    private Bundle bundle;

    @Cache(usage = NONSTRICT_READ_WRITE)
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "eventApplyProduct", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<EventProductApply> eventProductApplies = new ArrayList<>();
    @Setter
    @Column(name = "event_apply_count")
    private Long eventApplyCount;
    @Setter
    @Column(name = "sold_out")
    private Boolean soldOut;
    @Column(name = "sold_count")
    private Long soldCount;
    @Setter
    @Column(name = "musinsa_link")
    private String link;

    @Builder
    public ProductBase(Long id, String name, int price, String mainThumbnailImage, String subThumbnailImage,
                       int discountPrice, Category category, Long displayOrder, boolean visible, Bundle bundle, Event event, long eventApplyCount, Boolean soldOut, String link) {
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
        this.eventApplyCount = eventApplyCount;
        this.soldOut = soldOut;
        this.link = link;
        this.soldCount = 0L;
    }

    public boolean soldOut() {
        return this.soldOut;
    }

    public void incrementEventApplyCount() {
        this.eventApplyCount++;
    }

    public void updateLink(String link) {
        this.link = link;
    }

    public void addSoldCount(Long amount) {
        this.soldCount += amount;
    }

    public void updateSoldCount(Long count) {
        this.soldCount = count;
    }
}
