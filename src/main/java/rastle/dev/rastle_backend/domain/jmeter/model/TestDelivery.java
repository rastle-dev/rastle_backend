// package rastle.dev.rastle_backend.domain.jmeter.model;

// import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import org.hibernate.annotations.ColumnDefault;

// import static jakarta.persistence.GenerationType.IDENTITY;

// @Entity
// @Getter
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// @Table(name = "test_delivery")
// public class TestDelivery {
//     @Id
//     @GeneratedValue(strategy = IDENTITY)
//     private Long id;
//     private String address;
//     private String postcode;
//     private String email;
//     private String tel;
//     @Column(name = "user_name")
//     private String userName;
//     @ColumnDefault("0")
//     @Column(name = "delivery_price")
//     private Long deliveryPrice;
//     @ColumnDefault("0")
//     @Column(name = "island_delivery_price")
//     private Long islandDeliveryPrice;
//     private String msg;
//     private String deliveryService;

//     public static TestDelivery newDelivery() {
//         return TestDelivery.builder()
//             .address("test address")
//             .postcode("12341234")
//             .email("12341234@12341234")
//             .tel("01010101001")
//             .userName("qwerqwerqwer")
//             .deliveryPrice(3000L)
//             .islandDeliveryPrice(5000L)
//             .msg("12341234")
//             .deliveryService("12341234123")
//             .build();
//     }
// }
