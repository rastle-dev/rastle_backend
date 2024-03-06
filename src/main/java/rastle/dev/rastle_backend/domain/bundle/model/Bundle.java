package rastle.dev.rastle_backend.domain.bundle.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bundle")
public class Bundle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bundle_id")
    private Long id;
    private String name;
    @Column(name = "image_urls")
    private String imageUrls;
    private String description;
    @Column(name = "sale_start_time")
    private LocalDateTime saleStartTime;
    boolean visible;

    @OneToMany(mappedBy = "bundle", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ProductBase> bundleProducts = new ArrayList<>();

    @Builder
    public Bundle(String name, String imageUrls, String description, LocalDateTime saleStartTime, boolean visible) {
        this.name = name;
        this.imageUrls = imageUrls;
        this.description = description;
        this.saleStartTime = saleStartTime;
        this.visible = visible;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public BundleInfo toBundleInfo() {
        return BundleInfo.builder()
            .id(id)
            .description(description)
            .name(name)
            .imageUrls(imageUrls)
            .saleStartTime(saleStartTime)
            .visible(visible)
            .build();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSaleStartTime(LocalDateTime saleStartTime) {
        this.saleStartTime = saleStartTime;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
