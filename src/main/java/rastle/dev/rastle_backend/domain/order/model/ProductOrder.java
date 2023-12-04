package rastle.dev.rastle_backend.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 결제를 시작할때, 주문 정보를 관리하기 위힌 객체
 */
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ProductOrder {
    @Id
    String id;
}
