package rastle.dev.rastle_backend.domain.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.OrderCreateRequest;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.OrderCreateResponse;
import rastle.dev.rastle_backend.domain.order.repository.mongo.ProductOrderRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.MemberOrderRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.global.component.OrderNumberComponent;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductOrderRepository productOrderRepository;
    private final MemberOrderRepository memberOrderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderNumberComponent orderNumberComponent;
    @Transactional
    public OrderCreateResponse createMemberOrders(OrderCreateRequest orderCreateRequest) {

        return null;
    }
}
