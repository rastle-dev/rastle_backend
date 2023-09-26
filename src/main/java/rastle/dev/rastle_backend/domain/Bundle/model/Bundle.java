package rastle.dev.rastle_backend.domain.Bundle.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.Product.model.BundleProduct;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bundle", catalog = "rastle_db")
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
    private List<BundleProduct> bundleProducts = new ArrayList<>();

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
}
