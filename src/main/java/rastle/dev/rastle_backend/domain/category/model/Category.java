package rastle.dev.rastle_backend.domain.category.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ProductBase> categoryProducts = new ArrayList<>();

    @Builder
    public Category(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
