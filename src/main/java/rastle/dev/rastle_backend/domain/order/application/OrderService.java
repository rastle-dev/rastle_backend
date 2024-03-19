package rastle.dev.rastle_backend.domain.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.coupon.dto.CouponInfo;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.*;
import rastle.dev.rastle_backend.domain.order.dto.OrderSimpleInfo;
import rastle.dev.rastle_backend.domain.order.dto.request.OrderCancelRequest;
import rastle.dev.rastle_backend.domain.order.dto.response.OrderCancelResponse;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.global.component.OrderNumberComponent;
import rastle.dev.rastle_backend.global.component.PortOneComponent;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;
import rastle.dev.rastle_backend.global.error.exception.NotAuthorizedException;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static rastle.dev.rastle_backend.global.common.enums.OrderStatus.CANCELLED;
import static rastle.dev.rastle_backend.global.common.enums.OrderStatus.CREATED;
import static rastle.dev.rastle_backend.global.common.enums.PaymentStatus.CANCELED;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderProductRepository orderProductRepository;
    private final CouponRepository couponRepository;
    private final OrderNumberComponent orderNumberComponent;
    private final PortOneComponent portOneComponent;

    @Transactional
    public OrderCreateResponse createOrderDetail(Long memberId, OrderCreateRequest orderCreateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        if (!(member.getId() == 11L || member.getId() == 8L || member.getId() == 92L)) {
            throw new NotAuthorizedException();
        }
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(CREATED)
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

    @Transactional(readOnly = true)
    public Page<?> getMemberOrder(Long memberId, Pageable pageable) {
        Page<OrderSimpleInfo> simpleOrderInfoByMemberId = orderDetailRepository.findSimpleOrderInfoByMemberId(memberId, pageable);
        List<MemberOrderInfo> memberOrderInfos = new ArrayList<>();
        for (OrderSimpleInfo orderSimpleInfo : simpleOrderInfoByMemberId) {
            memberOrderInfos.add(new MemberOrderInfo(orderSimpleInfo, orderProductRepository.findSimpleProductOrderInfoByOrderId(orderSimpleInfo.getOrderId())));
        }
        return new PageImpl<MemberOrderInfo>(memberOrderInfos, pageable, simpleOrderInfoByMemberId.getTotalElements());

    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long memberId, Long orderId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        OrderDetail orderDetail = orderDetailRepository.findById(orderId).orElseThrow(NotFoundByIdException::new);
        if (!orderDetail.getMember().getId().equals(member.getId())) {
            throw new NotAuthorizedException("권한 없는 주문 조회 요청, memberId " + member.getId() + " orderId " + orderDetail.getId());
        }
        PaymentResponse paymentData = portOneComponent.getPaymentData(orderDetail.getImpId());
        CouponInfo couponInfo = couponRepository.findByCouponInfoById(paymentData.getCouponId()).orElse(null);

        LocalDateTime cancelTime = paymentData.getCancelledAt();


        return OrderDetailResponse.builder()
            .orderNumber(orderDetail.getOrderNumber())
            .orderDate(orderDetail.getCreatedTime().toString())
            .memberName(member.getUserName())
            .orderStatus(orderDetail.getOrderStatus())
            .deliveryStatus(orderDetail.getOrderStatus())
            .productOrderInfos(orderProductRepository.findSimpleProductOrderInfoByOrderId(orderDetail.getId()))
            .paymentAmount(orderDetail.getPaymentPrice())
            .deliveryPrice(orderDetail.getDeliveryPrice())
            .paymentMethod(paymentData.getPayMethod())
            .receiverInfo(
                ReceiverInfo.builder()
                    .receiverName(paymentData.getBuyerName())
                    .address(paymentData.getBuyerAddress())
                    .tel(paymentData.getBuyerTel())
                    .postcode(paymentData.getBuyerPostCode())
                    .build()
            )
            .deliveryMsg(paymentData.getDeliveryMsg())
            .refundInfo(
                RefundInfo.builder()
                    .cancelAmount(paymentData.getCancelAmount())
                    .couponInfo(couponInfo)
                    .cancelTime(cancelTime)
                    .paymentMethod(paymentData.getPayMethod())
                    .build()
            )
            .build();
    }

    @Transactional
    public OrderCancelResponse cancelOrder(Long memberId, OrderCancelRequest orderCancelRequest) {
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(orderCancelRequest.getMerchantUID()).orElseThrow(() -> new RuntimeException("해당 주문 번호로 존재하는 주문이 없습니다. " + orderCancelRequest.getMerchantUID()));
        PaymentResponse paymentResponse = portOneComponent.cancelPayment(orderDetail.getImpId(), orderCancelRequest);
        orderDetail.updateOrderStatus(CANCELLED);

        return OrderCancelResponse.builder()
            .cancelledAt(paymentResponse.getCancelledAt())
            .cancelAmount(paymentResponse.getCancelAmount())
            .cancelHistory(paymentResponse.getCancelHistory())
            .cancelReason(paymentResponse.getCancelReason())
            .paymentStatus(CANCELED)
            .merchantUID(paymentResponse.getMerchantUID())
            .build();
    }
}
