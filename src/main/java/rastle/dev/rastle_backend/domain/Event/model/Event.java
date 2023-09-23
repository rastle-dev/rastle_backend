package rastle.dev.rastle_backend.domain.Event.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.Product.model.EventProduct;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "event", catalog = "rastle_db")
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


    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<EventProduct> eventProducts = new ArrayList<>();

    @Builder
    public Event(String name, LocalDateTime eventStartDate, LocalDateTime eventEndDate, String imageUrls, String description) {
        this.name = name;
        this.eventStartDate = eventStartDate;
        this.eventEndDate = eventEndDate;
        this.imageUrls = imageUrls;
        this.description = description;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }
}
