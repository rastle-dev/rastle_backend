package rastle.dev.rastle_backend.domain.Event.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Product.model.ProductBase;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "event_product", catalog = "rastle_db")
public class EventProduct extends ProductBase {
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
