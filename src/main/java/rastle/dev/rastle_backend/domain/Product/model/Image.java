//package rastle.dev.rastle_backend.domain.Product.model;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@NoArgsConstructor
//@Table(name = "image", catalog = "rastle_db")
//public class Image {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "image_id")
//    private Long id;
//
//    @Column(name = "image_url")
//    private String imageUrl;
//
//    @ManyToOne
//    @JoinColumn(name = "product_image_id")
//    private ProductImage productImage;
//
//    public Image(String imageUrl, ProductImage productImage) {
//        this.imageUrl = imageUrl;
//        this.productImage = productImage;
//    }
//}
