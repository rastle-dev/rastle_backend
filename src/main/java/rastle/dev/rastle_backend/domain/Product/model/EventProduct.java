package rastle.dev.rastle_backend.domain.Product.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Category.model.Category;
import rastle.dev.rastle_backend.domain.Event.model.Event;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "event_product", catalog = "rastle_db")
public class EventProduct extends ProductBase {
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Builder
    public EventProduct(String name, int price, boolean isEventProduct, String mainThumbnailImage,
            String subThumbnailImage, Event event, int discount, Category category, Long displayOrder, boolean visible) {
        super(name, price, isEventProduct, mainThumbnailImage, subThumbnailImage, discount, category, displayOrder, visible);
        this.event = event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
