package rastle.dev.rastle_backend.domain.event.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    private String name;
    @Column(name = "image_urls")
    private String imageUrls;
    @Column(name = "event_start_date")
    private LocalDateTime eventStartDate;
    @Column(name = "event_end_date")
    private LocalDateTime eventEndDate;
    private String description;
    private boolean visible;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ProductBase> eventProducts = new ArrayList<>();

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

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setEventStartDate(LocalDateTime eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public void setEventEndDate(LocalDateTime eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
