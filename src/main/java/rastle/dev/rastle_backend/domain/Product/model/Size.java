package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "size", catalog = "rastle_db")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    private Long id;

    private String name;
    private int count;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private Color color;

    public Size(String name, int count, Color color) {
        this.name = name;
        this.count = count;
        this.color = color;
    }
}
