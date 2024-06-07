package rastle.dev.rastle_backend.domain.order.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.cart.dto.CartOrderRequest;
import rastle.dev.rastle_backend.domain.cart.model.CartProduct;
import rastle.dev.rastle_backend.domain.cart.repository.mysql.CartProductRepository;
import rastle.dev.rastle_backend.domain.coupon.dto.CouponInfo;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.domain.delivery.model.Delivery;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.domain.order.dto.OrderDTO.*;
import rastle.dev.rastle_backend.domain.order.dto.OrderSimpleInfo;
import rastle.dev.rastle_backend.domain.order.dto.request.OrderCancelRequest;
import rastle.dev.rastle_backend.domain.order.dto.request.ProductOrderCancelRequest;
import rastle.dev.rastle_backend.domain.order.dto.response.OrderCancelResponse;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.payment.model.Payment;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.domain.product.repository.mysql.ProductBaseRepository;
import rastle.dev.rastle_backend.global.component.OrderNumberComponent;
import rastle.dev.rastle_backend.global.component.PortOneComponent;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;
import rastle.dev.rastle_backend.global.error.exception.GlobalException;
import rastle.dev.rastle_backend.global.error.exception.NotAuthorizedException;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
import static rastle.dev.rastle_backend.global.common.enums.OrderStatus.CANCEL_REQUESTED;
import static rastle.dev.rastle_backend.global.common.enums.OrderStatus.CREATED;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final CartProductRepository cartProductRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderProductRepository orderProductRepository;
    private final CouponRepository couponRepository;
    private final OrderNumberComponent orderNumberComponent;
    private final PortOneComponent portOneComponent;
    private final ProductBaseRepository productBaseRepository;

    @Transactional
    public OrderCreateResponse createOrderDetail(Long memberId, OrderCreateRequest orderCreateRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        verifyTestMember(member);
        OrderDetail orderDetail = createNewOrder(member);
        setOrderNumber(orderDetail);

        return OrderCreateResponse.builder()
            .orderDetailId(orderDetail.getId())
            .orderNumber(orderDetail.getOrderNumber().toString())
            .orderProducts(createOrderProducts(orderDetail,
                orderCreateRequest.getOrderProducts()))
            .build();
    }

    private void verifyTestMember(Member member) {
        if (!(member.getId() == 11L || member.getId() == 8L || member.getId() == 92L || member.getId() == 47L)) {
            throw new NotAuthorizedException();
        }
    }

    private OrderDetail createNewOrder(Member member) {
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(CREATED)
            .member(member)
            .build();
        orderDetailRepository.save(orderDetail);
        setDeliveryAndPayment(orderDetail);
        return orderDetail;
    }

    private void setOrderNumber(OrderDetail orderDetail) {
        Long orderNumber = orderNumberComponent.createOrderNumber(orderDetail.getId());
        orderDetail.updateOrderNumber(orderNumber);
    }

    private void setDeliveryAndPayment(OrderDetail orderDetail) {
        orderDetail.setDelivery(Delivery.builder().orderDetail(orderDetail).build());
        orderDetail.setPayment(Payment.builder().orderDetail(orderDetail).cancelledSum(0L).couponAmount(0L).build());
    }

    private List<ProductOrderResponse> createOrderProducts(OrderDetail orderDetail,
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
                .totalPrice((long) productBase.getDiscountPrice() * productOrderRequest.getCount())
                .cancelAmount(0L)
                .cancelRequestAmount(0L)
                .build();
            orderProductRepository.save(orderProduct);
            orderPrice += orderProduct.getTotalPrice();

            Long productOrderNumber = orderNumberComponent.createOrderNumber(
                orderProduct.getId());

            orderProduct.updateProductOrderNumber(productOrderNumber);
            orderResponses.add(toProductOrderResponse(orderProduct));

        }
        orderDetail.updateOrderPrice(orderPrice);
        return orderResponses;
    }

    @Transactional(readOnly = true)
    public Page<?> getMemberOrder(Long memberId, Pageable pageable) {
        Page<OrderSimpleInfo> simpleOrderInfoByMemberId = orderDetailRepository.findSimpleOrderInfoByMemberId(memberId, pageable);
        List<MemberOrderInfo> memberOrderInfos = new ArrayList<>();
        for (OrderSimpleInfo orderSimpleInfo : simpleOrderInfoByMemberId) {
            memberOrderInfos.add(new MemberOrderInfo(orderSimpleInfo, orderProductRepository.findSimpleProductOrderInfoByOrderId(orderSimpleInfo.getOrderId())));
        }
        return new PageImpl<>(memberOrderInfos, pageable, simpleOrderInfoByMemberId.getTotalElements());

    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long memberId, Long orderId) {
        OrderDetail orderDetail = orderDetailRepository.findByIdAndMemberId(orderId, memberId).orElseThrow(NotFoundByIdException::new);
        return getOrderDetailResponse(orderDetail);
    }

    private OrderDetailResponse getOrderDetailResponse(OrderDetail orderDetail) {

        PaymentResponse paymentData = portOneComponent.getPaymentData(orderDetail.getPayment().getImpId());
        CouponInfo couponInfo = couponRepository.findByCouponInfoById(paymentData.getCouponId()).orElse(null);

        LocalDateTime cancelTime = paymentData.getCancelledAt();


        return OrderDetailResponse.builder()
            .orderNumber(orderDetail.getOrderNumber().toString())
            .orderDate(orderDetail.getCreatedTime().toString())
            .memberName(orderDetail.getMember().getUserName())
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
            .couponAmount(orderDetail.getPayment().getCouponAmount())
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

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long memberId, String merchantId) {
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumberAndMemberId(Long.parseLong(merchantId), memberId).orElseThrow(NotFoundByIdException::new);
        return getOrderDetailResponse(orderDetail);
    }

    private void validateMemberOrder(Member member, OrderDetail orderDetail) {
        if (!orderDetail.getMember().getId().equals(member.getId())) {
            throw new NotAuthorizedException("권한 없는 주문 조회 요청, memberId " + member.getId() + " orderId " + orderDetail.getId());
        }
    }

    @Transactional
    public OrderCancelResponse cancelOrder(Long memberId, OrderCancelRequest orderCancelRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumberAndMemberId(orderCancelRequest.getOrderNumber(), memberId).orElseThrow(() -> new RuntimeException("해당 주문 번호로 존재하는 주문이 없습니다. " + orderCancelRequest.getOrderNumber()));
        validateMemberOrder(member, orderDetail);

        for (ProductOrderCancelRequest productOrderCancelRequest : orderCancelRequest.getProductOrderCancelRequests()) {
            Optional<OrderProduct> byProductOrderNumber = orderProductRepository.findByProductOrderNumber(productOrderCancelRequest.getProductOrderNumber());
            if (byProductOrderNumber.isPresent()) {
                OrderProduct orderProduct = byProductOrderNumber.get();
                if (orderProduct.getCount() < orderProduct.getCancelAmount() + orderProduct.getCancelRequestAmount()+ productOrderCancelRequest.getCancelAmount() || productOrderCancelRequest.getCancelAmount() <= 0) {
                    throw new GlobalException("유효하지 않은 취소 수량으로 인한 요청 처리 실패", CONFLICT);
                }
                orderProduct.updateOrderStatus(CANCEL_REQUESTED);
                orderProduct.addCancelRequestAmount(productOrderCancelRequest.getCancelAmount());
            }

        }
        orderDetail.updateOrderStatus(CANCEL_REQUESTED);


        return OrderCancelResponse.builder()
            .cancelProductOrders(orderCancelRequest.getProductOrderCancelRequests())
            .build();
    }

    @Transactional
    public OrderCreateResponse createCartOrder(Long memberId, CartOrderRequest cartOrderRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
        verifyTestMember(member);
        OrderDetail orderDetail = createNewOrder(member);
        setOrderNumber(orderDetail);
        return OrderCreateResponse.builder()
            .orderDetailId(orderDetail.getId())
            .orderNumber(orderDetail.getOrderNumber().toString())
            .orderProducts(createOrderProductsFromCart(orderDetail,
                cartOrderRequest))
            .build();
    }

    private List<ProductOrderResponse> createOrderProductsFromCart(OrderDetail orderDetail, CartOrderRequest cartOrderRequest) {
        Long orderPrice = 0L;
        List<ProductOrderResponse> orderResponses = new ArrayList<>();
        for (Long cartProductId : cartOrderRequest.getCartProductIds()) {
            Optional<CartProduct> cartProductOptional = cartProductRepository.findCartProductAndProductById(cartProductId);
            if (cartProductOptional.isPresent()) {
                CartProduct cartProduct = cartProductOptional.get();
                ProductBase productBase = cartProduct.getProduct();
                OrderProduct orderProduct = OrderProduct.builder()
                    .orderDetail(orderDetail)
                    .orderStatus(CREATED)
                    .product(ProductBase.builder().id(productBase.getId()).build())
                    .name(productBase.getName())
                    .color(cartProduct.getColor())
                    .size(cartProduct.getSize())
                    .count((long) cartProduct.getCount())
                    .price((long) productBase.getDiscountPrice())
                    .totalPrice((long) productBase.getDiscountPrice() * cartProduct.getCount())
                    .cancelAmount(0L)
                    .cancelRequestAmount(0L)
                    .cartProductId(cartProductId)
                    .build();
                orderProductRepository.save(orderProduct);
                orderPrice += orderProduct.getTotalPrice();

                Long productOrderNumber = orderNumberComponent.createOrderNumber(
                    orderProduct.getId());

                orderProduct.updateProductOrderNumber(productOrderNumber);
                orderResponses.add(toProductOrderResponse(orderProduct));
            }
        }
        orderDetail.updateOrderPrice(orderPrice);
        return orderResponses;
    }

    private ProductOrderResponse toProductOrderResponse(OrderProduct orderProduct) {
        return ProductOrderResponse.builder()
            .productId(orderProduct.getProduct().getId())
            .name(orderProduct.getName())
            .color(orderProduct.getColor())
            .size(orderProduct.getSize())
            .count(orderProduct.getCount())
            .totalPrice(orderProduct.getTotalPrice())
            .productOrderNumber(String.valueOf(orderProduct.getProductOrderNumber()))
            .build();
    }
}
