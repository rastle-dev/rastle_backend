package rastle.dev.rastle_backend.domain.hibernate.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Cache;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Cacheable
@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TestMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<TestCartProduct> cartProducts = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final List<TestOrder> orders = new ArrayList<>();

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
}
