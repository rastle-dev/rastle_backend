package rastle.dev.rastle_backend.domain.event.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "event_product_apply")
public class EventProductApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "instagram_id")
    private String instagramId;

    @CreatedDate
    @Column(name = "apply_date", updatable = false)
    private LocalDateTime applyDate;

    @ManyToOne
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
