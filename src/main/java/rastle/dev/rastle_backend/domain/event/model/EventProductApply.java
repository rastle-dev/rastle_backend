package rastle.dev.rastle_backend.domain.event.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "event_product_apply")
public class EventProductApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "instagram_id")
    private String instagramId;

    @Column(name = "apply_date")
    private LocalDateTime applyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductBase eventApplyProduct;

    @Builder
    public EventProductApply(Member member, String phoneNumber, String instagramId, ProductBase eventApplyProduct) {
        this.member = member;
        this.phoneNumber = phoneNumber;
        this.instagramId = instagramId;
        this.eventApplyProduct = eventApplyProduct;
    }

    public void update(String phoneNumber, String instagramId) {
        this.phoneNumber = phoneNumber;
        this.applyDate = LocalDateTime.now();
        this.instagramId = instagramId;
    }
}
