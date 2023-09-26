package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Category.model.Category;
import rastle.dev.rastle_backend.domain.Bundle.model.Bundle;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bundle_product", catalog = "rastle_db")
public class BundleProduct extends ProductBase {
    @ManyToOne
    @JoinColumn(name = "bundle_id")
    private Bundle bundle;

    @Builder
    public BundleProduct(String name, int price, boolean isEventProduct, String mainThumbnailImage,
                         String subThumbnailImage, Bundle bundle, int discount, Category category, Long displayOrder, boolean visible) {
        super(name, price, isEventProduct, mainThumbnailImage, subThumbnailImage, discount, category, displayOrder, visible);
        this.bundle = bundle;
    }
}
