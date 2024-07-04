package rastle.dev.rastle_backend.domain.event.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import rastle.dev.rastle_backend.domain.event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Cache(usage = READ_WRITE)
@Cacheable
@Entity
@Getter
@NoArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @Setter
    private String name;
    @Setter
    @Column(name = "image_urls")
    private String imageUrls;
    @Setter
    @Column(name = "event_start_date")
    private LocalDateTime eventStartDate;
    @Setter
    @Column(name = "event_end_date")
    private LocalDateTime eventEndDate;
    @Setter
    private String description;
    @Setter
    private boolean visible;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private final List<ProductBase> eventProducts = new ArrayList<>();

    @Builder
    public Event(String name, LocalDateTime eventStartDate, LocalDateTime eventEndDate, String imageUrls,
                 String description, boolean visible) {
        this.name = name;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.imageUrls = imageUrls;
        this.description = description;
        this.visible = visible;
    }

    public EventInfo toEventInfo() {
        return EventInfo.builder()
            .id(this.id)
            .imageUrls(imageUrls)
            .name(name)
            .description(description)
            .startDate(getEventStartDate())
            .endDate(getEventEndDate())
            .visible(visible)
            .build();
    }

}
