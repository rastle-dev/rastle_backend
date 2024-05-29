package rastle.dev.rastle_backend.domain.cart.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import rastle.dev.rastle_backend.domain.member.model.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @Builder
    public Cart(Member member) {
        this.member = member;
    }
}
