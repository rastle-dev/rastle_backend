package rastle.dev.rastle_backend.domain.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.ProductOrderRequest;

import java.util.List;

/**
 * 결제를 시작할때, 주문 정보를 관리하기 위힌 객체, mongodb용, 안쓸 객체
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class ProductOrder {
    @Id
    String id;
    String orderNumber;
    List<ProductOrderRequest> orderProducts;
}
