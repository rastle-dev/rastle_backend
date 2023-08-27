package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Event.model.Event;
import rastle.dev.rastle_backend.domain.Orders.model.OrderProduct;
import rastle.dev.rastle_backend.domain.Product.model.ProductBase;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "event_product", catalog = "rastle_db")
public class EventProduct extends ProductBase {
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Builder
    public EventProduct(String name, int price, boolean isEventProduct, String mainThumbnailImage, String subThumbnailImage, Event event) {
        super(name, price, isEventProduct, mainThumbnailImage, subThumbnailImage);
        this.event = event;
    }
}
