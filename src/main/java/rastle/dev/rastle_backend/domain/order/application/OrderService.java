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

import static rastle.dev.rastle_backend.global.common.enums.DeliveryStatus.NOT_STARTED;
import static rastle.dev.rastle_backend.global.common.enums.PaymentStatus.READY;

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
        PaymentResponse paymentData = portOneComponent.getPaymentData(orderDetail.getImpId());
        CouponInfo couponInfo = couponRepository.findByCouponInfoById(paymentData.getCouponId()).orElse(null);

        LocalDateTime cancelTime = paymentData.getCancelledAt();


        return OrderDetailResponse.builder()
            .orderNumber(orderDetail.getOrderNumber())
            .orderDate(orderDetail.getCreatedTime().toString())
            .memberName(member.getUserName())
            .deliveryStatus(orderDetail.getDeliveryStatus())
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
}
