package rastle.dev.rastle_backend.domain.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.OrderCreateRequest;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.OrderCreateResponse;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.ProductOrderRequest;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.ProductOrderResponse;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.global.component.OrderNumberComponent;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

import java.util.ArrayList;
import java.util.List;

import static rastle.dev.rastle_backend.global.common.enums.DeliveryStatus.NOT_STARTED;
import static rastle.dev.rastle_backend.global.common.enums.PaymentStatus.READY;

@Service
@RequiredArgsConstructor
public class OrderService {
        private final MemberRepository memberRepository;
        private final OrderDetailRepository orderDetailRepository;
        private final OrderProductRepository orderProductRepository;
        private final OrderNumberComponent orderNumberComponent;

        @Transactional
        public OrderCreateResponse createOrderDetail(Long memberId, OrderCreateRequest orderCreateRequest) {
                Member member = null;
                if (memberId != null) {
                        member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
                }
                OrderDetail orderDetail = OrderDetail.builder()
                                .deliveryStatus(NOT_STARTED)
                                .paymentStatus(READY)
                                .member(member)
                                .build();
                orderDetailRepository.save(orderDetail);
                String orderNumber = orderNumberComponent.createOrderNumber(orderDetail.getId());
                orderDetail.updateOrderNumber(orderNumber);
                List<ProductOrderResponse> orderResponses = createOrderProducts(orderDetail,
                                orderCreateRequest.getOrderProducts());

                return OrderCreateResponse.builder()
                                .orderDetailId(orderDetail.getId())
                                .orderNumber(orderNumber)
                                .orderProducts(orderResponses)
                                .build();
        }

        private List<ProductOrderResponse> createOrderProducts(OrderDetail orderDetail,
                        List<ProductOrderRequest> productOrderRequests) {
                List<ProductOrderResponse> orderResponses = new ArrayList<>();

                for (ProductOrderRequest productOrderRequest : productOrderRequests) {
                        OrderProduct orderProduct = OrderProduct.builder()
                                        .orderDetail(orderDetail)
                                        .product(ProductBase.builder().id(productOrderRequest.getProductId()).build())
                                        .name(productOrderRequest.getName())
                                        .color(productOrderRequest.getColor())
                                        .size(productOrderRequest.getSize())
                                        .count(productOrderRequest.getCount())
                                        .totalPrice(productOrderRequest.getTotalPrice())
                                        .build();
                        orderProductRepository.save(orderProduct);

                        String productOrderNumber = orderNumberComponent.createProductOrderNumber(orderDetail.getId(),
                                        orderProduct.getId());

                        orderProduct.updateProductOrderNumber(productOrderNumber);
                        orderResponses.add(ProductOrderRequest.toResponse(productOrderRequest, productOrderNumber));
                }

                return orderResponses;
        }
}
