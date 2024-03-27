package rastle.dev.rastle_backend.domain.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.coupon.dto.CouponInfo;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.domain.delivery.model.Delivery;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.*;
import rastle.dev.rastle_backend.domain.order.dto.OrderProductSummary;
import rastle.dev.rastle_backend.domain.order.dto.OrderSimpleInfo;
import rastle.dev.rastle_backend.domain.order.dto.request.OrderCancelRequest;
import rastle.dev.rastle_backend.domain.order.dto.response.OrderCancelResponse;
import rastle.dev.rastle_backend.domain.order.model.CancelRequest;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;
import rastle.dev.rastle_backend.domain.order.repository.mysql.CancelRequestRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.payment.model.Payment;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductBaseRepository;
import rastle.dev.rastle_backend.global.component.OrderNumberComponent;
import rastle.dev.rastle_backend.global.component.PortOneComponent;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;
import rastle.dev.rastle_backend.global.error.exception.NotAuthorizedException;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static rastle.dev.rastle_backend.global.common.enums.OrderStatus.CREATED;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderProductRepository orderProductRepository;
    private final CouponRepository couponRepository;
    private final OrderNumberComponent orderNumberComponent;
    private final PortOneComponent portOneComponent;
    private final ProductBaseRepository productBaseRepository;
    private final CancelRequestRepository cancelRequestRepository;

    @Transactional
    public OrderCreateResponse createOrderDetail(Long memberId, OrderCreateRequest orderCreateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        if (!(member.getId() == 11L || member.getId() == 8L || member.getId() == 92L || member.getId() == 47L)) {
            throw new NotAuthorizedException();
        }
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(CREATED)
            .member(member)
            .build();

        orderDetailRepository.save(orderDetail);
        orderDetail.setDelivery(Delivery.builder().orderDetail(orderDetail).build());
        orderDetail.setPayment(Payment.builder().orderDetail(orderDetail).build());
        Long orderNumber = orderNumberComponent.createOrderNumber(orderDetail.getId());
        orderDetail.updateOrderNumber(orderNumber);
        OrderProductSummary orderResponses = createOrderProducts(orderDetail,
            orderCreateRequest.getOrderProducts());
        orderDetail.updateOrderPrice(orderResponses.getOrderPrice());

        return OrderCreateResponse.builder()
            .orderDetailId(orderDetail.getId())
            .orderNumber(orderNumber.toString())
            .orderProducts(orderResponses.getProductOrderResponses())
            .build();
    }

    private OrderProductSummary createOrderProducts(OrderDetail orderDetail,
                                                    List<ProductOrderRequest> productOrderRequests) {
        Long orderPrice = 0L;
        List<ProductOrderResponse> orderResponses = new ArrayList<>();
        for (ProductOrderRequest productOrderRequest : productOrderRequests) {
            ProductBase productBase = productBaseRepository.getReferenceById(productOrderRequest.getProductId());
            OrderProduct orderProduct = OrderProduct.builder()
                .orderDetail(orderDetail)
                .orderStatus(CREATED)
                .product(ProductBase.builder().id(productOrderRequest.getProductId()).build())
                .name(productOrderRequest.getName())
                .color(productOrderRequest.getColor())
                .size(productOrderRequest.getSize())
                .count(productOrderRequest.getCount())
                .price((long) productBase.getDiscountPrice())
                .totalPrice(productBase.getDiscountPrice() * productOrderRequest.getCount())
                .build();
            orderProductRepository.save(orderProduct);
            orderPrice += orderProduct.getTotalPrice();

            Long productOrderNumber = orderNumberComponent.createProductOrderNumber(orderDetail.getId(),
                orderProduct.getId());

            orderProduct.updateProductOrderNumber(productOrderNumber);
            orderResponses.add(ProductOrderRequest.toResponse(productOrderRequest, productOrderNumber.toString()));
        }

        return new OrderProductSummary(orderPrice, orderResponses);
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
        validateMemberOrder(member, orderDetail);

        PaymentResponse paymentData = portOneComponent.getPaymentData(orderDetail.getPayment().getImpId());
        CouponInfo couponInfo = couponRepository.findByCouponInfoById(paymentData.getCouponId()).orElse(null);

        LocalDateTime cancelTime = paymentData.getCancelledAt();


        return OrderDetailResponse.builder()
            .orderNumber(orderDetail.getOrderNumber().toString())
            .orderDate(orderDetail.getCreatedTime().toString())
            .memberName(member.getUserName())
            .orderStatus(orderDetail.getOrderStatus())
            .deliveryStatus(orderDetail.getOrderStatus())
            .productOrderInfos(orderProductRepository.findSimpleProductOrderInfoByOrderId(orderDetail.getId()))
            .paymentAmount(orderDetail.getPayment().getPaymentPrice())
            .deliveryPrice(orderDetail.getDelivery().getDeliveryPrice())
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

    private void validateMemberOrder(Member member, OrderDetail orderDetail) {
        if (!orderDetail.getMember().getId().equals(member.getId())) {
            throw new NotAuthorizedException("권한 없는 주문 조회 요청, memberId " + member.getId() + " orderId " + orderDetail.getId());
        }
    }

    // TODO 주문 취소 요청이 왔을 때, 주문 상태에 따른 서비스 로직 분기 처리를 해야함

    @Transactional
    public OrderCancelResponse cancelOrder(Long memberId, OrderCancelRequest orderCancelRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(orderCancelRequest.getOrderNumber()).orElseThrow(() -> new RuntimeException("해당 주문 번호로 존재하는 주문이 없습니다. " + orderCancelRequest.getOrderNumber()));
        validateMemberOrder(member, orderDetail);
        // cancel request 만 생성하면될듯??
        List<CancelRequest> toSave = new ArrayList<>();

        for (Long productOrderNumber : orderCancelRequest.getProductOrderNumber()) {
            CancelRequest cancelRequest = CancelRequest.builder().orderDetail(orderDetail).reason(orderCancelRequest.getReason()).productOrderNumber(productOrderNumber).build();
            toSave.add(cancelRequest);
        }

        cancelRequestRepository.saveAll(toSave);

        // Long cancelAmount = 0L;
        // for (Long productOrderNumber : orderCancelRequest.getProductOrderNumber()) {
        //     OrderProduct orderProduct = orderProductRepository.findByProductOrderNumber(productOrderNumber
        //     ).orElseThrow(() -> new RuntimeException("해당 상품 주문번호로 존재하는 상품 주문이 없다. " + productOrderNumber));
        //     cancelAmount += orderProduct.getTotalPrice();
        // }

        // PaymentResponse paymentResponse = portOneComponent.cancelPayment(orderDetail.getPayment().getImpId(), cancelAmount, orderDetail);
        // orderDetail.updateOrderStatus(CANCELLED);
        // orderDetail.getPayment().addCancelledSum(cancelAmount);

        return OrderCancelResponse.builder()
            .cancelProductOrderNumber(orderCancelRequest.getProductOrderNumber())
            .build();
    }
}
