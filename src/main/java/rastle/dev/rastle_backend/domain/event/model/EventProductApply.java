package rastle.dev.rastle_backend.domain.event.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
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

    @CreatedDate
    @Column(name = "apply_date", updatable = false)
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
}
