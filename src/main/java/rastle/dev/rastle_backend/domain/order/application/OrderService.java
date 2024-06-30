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
import rastle.dev.rastle_backend.domain.order.dto.SimpleProductOrderInfo;
import rastle.dev.rastle_backend.domain.order.dto.request.OrderCancelRequest;
import rastle.dev.rastle_backend.domain.order.dto.request.OrderReturnRequest;
import rastle.dev.rastle_backend.domain.order.dto.request.ProductOrderCancelRequest;
import rastle.dev.rastle_backend.domain.order.dto.request.ProductReturnRequest;
import rastle.dev.rastle_backend.domain.order.dto.response.OrderCancelResponse;
import rastle.dev.rastle_backend.domain.order.dto.response.OrderReturnResponse;
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
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static rastle.dev.rastle_backend.global.common.enums.OrderStatus.*;

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
        OrderDetail orderDetail = createNewOrder(member);
        setOrderNumber(orderDetail);

        return OrderCreateResponse.builder()
            .orderDetailId(orderDetail.getId())
            .orderNumber(orderDetail.getOrderNumber().toString())
            .orderProducts(createOrderProducts(orderDetail,
                orderCreateRequest.getOrderProducts()))
            .build();
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
            if (productBase.soldOut()) {
                throw new GlobalException("품절된 상품으로 구매가 불가합니다.", CONFLICT);
            }
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
                .returnAmount(0L)
                .returnRequestAmount(0L)
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
        List<OrderSimpleInfo> orderSimpleInfos = orderDetailRepository.findSimpleOrderInfoByMemberId(memberId, (long) pageable.getPageSize(), pageable.getOffset()).stream().map(OrderSimpleInfo::fromInterface).toList();
        List<SimpleProductOrderInfo> simpleProductOrderInfos = orderProductRepository.findSimpleProductOrderInfoByMemberId(memberId).stream().map(SimpleProductOrderInfo::fromInterface).toList();
        List<MemberOrderInfo> memberOrderInfos = new ArrayList<>();
        for (OrderSimpleInfo orderSimpleInfo : orderSimpleInfos) {
            List<SimpleProductOrderInfo> orderProducts = new ArrayList<>();
            for (SimpleProductOrderInfo productOrderInfo : simpleProductOrderInfos) {
                if (productOrderInfo.getOrderNumber().toString().equals(orderSimpleInfo.getOrderNumber())) {
                    orderProducts.add(productOrderInfo);
                }
            }
            memberOrderInfos.add(new MemberOrderInfo(orderSimpleInfo, orderProducts));
        }
        return new PageImpl<>(memberOrderInfos, pageable, orderDetailRepository.countSimpleOrderInfoByMemberId(memberId));

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
            .productOrderInfos(orderProductRepository.findSimpleProductOrderInfoByOrderId(orderDetail.getId()).stream().map(SimpleProductOrderInfo::fromInterface).toList())
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
            .pgProvider(paymentData.getPgProvider())
            .embPgProvider(paymentData.getEmbPgProvider())
            .build();
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long memberId, String merchantId) {
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumberAndMemberId(Long.parseLong(merchantId), memberId).orElseThrow(NotFoundByIdException::new);
        return getOrderDetailResponse(orderDetail);
    }


    @Transactional
    public OrderCancelResponse cancelOrder(Long memberId, OrderCancelRequest orderCancelRequest) {
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumberAndMemberId(orderCancelRequest.getOrderNumber(), memberId).orElseThrow(() -> new RuntimeException("해당 주문 번호로 존재하는 주문이 없습니다. " + orderCancelRequest.getOrderNumber()));

        for (ProductOrderCancelRequest productOrderCancelRequest : orderCancelRequest.getProductOrderCancelRequests()) {
            Optional<OrderProduct> byProductOrderNumber = orderProductRepository.findByProductOrderNumberWithLock(productOrderCancelRequest.getProductOrderNumber());
            if (byProductOrderNumber.isPresent()) {
                OrderProduct orderProduct = byProductOrderNumber.get();
                if (isValidCancelRequest(orderProduct, productOrderCancelRequest)) {
                    orderProduct.updateOrderStatus(CANCEL_REQUESTED);
                    orderProduct.addCancelRequestAmount(productOrderCancelRequest.getCancelAmount());
                } else {
                    handleInvalidCancelRequest(orderProduct, productOrderCancelRequest);
                }
            } else {
                throw new GlobalException("해당하는 상품 주문 번호로 존재하는 상품 주문이 없기에 취소 요청 생성에 실패", NOT_FOUND);
            }
        }
        orderDetail.updateOrderStatus(CANCEL_REQUESTED);
        return OrderCancelResponse.builder()
            .cancelProductOrders(orderCancelRequest.getProductOrderCancelRequests())
            .build();
    }

    private boolean validateOrderProductStatusForCancel(OrderProduct orderProduct) {
        return orderProduct.getOrderStatus().equals(PAID) || orderProduct.getOrderStatus().equals(PARTIALLY_CANCELLED) || orderProduct.getOrderStatus().equals(CANCEL_REQUESTED);
    }

    private boolean isValidCancelRequest(OrderProduct orderProduct, ProductOrderCancelRequest productOrderCancelRequest) {
        return validateOrderProductStatusForCancel(orderProduct) && orderProduct.getCount() >= orderProduct.getCancelAmount() + orderProduct.getCancelRequestAmount() + productOrderCancelRequest.getCancelAmount() && productOrderCancelRequest.getCancelAmount() > 0;
    }

    private void handleInvalidCancelRequest(OrderProduct orderProduct, ProductOrderCancelRequest productOrderCancelRequest) {
        if (orderProduct.getOrderStatus().equals(DELIVERY_READY) || orderProduct.getOrderStatus().equals(DELIVERY_STARTED) || orderProduct.getOrderStatus().equals(DELIVERED)) {
            throw new GlobalException("배송이 시작된 경우 주문 취소가 아닌 반품 신청을 해야합니다.", CONFLICT);
        }
        if (orderProduct.getOrderStatus().equals(COMPLETED)) {
            throw new GlobalException("주문 완료 처리가 된 주문으로 취소 신청이 불가합니다.", CONFLICT);
        }
        if (productOrderCancelRequest.getCancelAmount() <= 0) {
            throw new GlobalException("유효하지 않은 취소 수량으로 인한 요청 처리 실패", CONFLICT);
        }
        if (orderProduct.getCount() < orderProduct.getCancelAmount() + orderProduct.getCancelRequestAmount() + productOrderCancelRequest.getCancelAmount()) {
            throw new GlobalException("취소 가능한 수량 보다 더 많은 수량에 대한 취소 요청으로 인한 처리 실패", CONFLICT);
        }

    }

    @Transactional
    public OrderCreateResponse createCartOrder(Long memberId, CartOrderRequest cartOrderRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundByIdException::new);
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
                if (productBase.soldOut()) {
                    throw new GlobalException("품절된 상품 포함으로 주문이 불가합니다.", CONFLICT);
                }
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
                    .returnAmount(0L)
                    .returnRequestAmount(0L)
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

    @Transactional
    public OrderReturnResponse returnOrder(Long memberId, OrderReturnRequest orderReturnRequest) {
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumberAndMemberId(orderReturnRequest.getOrderNumber(), memberId).orElseThrow(() -> new GlobalException("해당 주문 번호로 존재하는 주문이 없습니다. " + orderReturnRequest.getOrderNumber(), CONFLICT));

        for (ProductReturnRequest productReturnRequest : orderReturnRequest.getProductReturnRequests()) {
            Optional<OrderProduct> byProductOrderNumber = orderProductRepository.findByProductOrderNumberWithLock(productReturnRequest.getProductOrderNumber());
            if (byProductOrderNumber.isPresent()) {
                OrderProduct orderProduct = byProductOrderNumber.get();
                if (isValidReturnRequest(orderProduct, productReturnRequest)) {
                    orderProduct.updateOrderStatus(RETURN_REQUESTED);
                    orderProduct.addReturnRequestAmount(productReturnRequest.getReturnAmount());
                } else {
                    handleInvalidReturnRequest(orderProduct, productReturnRequest);
                }
            } else {
                throw new GlobalException("해당하는 상품 주문 번호로 존재하는 상품 주문이 없기에 취소 요청 생성에 실패", NOT_FOUND);
            }
        }

        orderDetail.updateOrderStatus(RETURN_REQUESTED);

        return new OrderReturnResponse(orderReturnRequest.getProductReturnRequests());
    }

    private boolean isValidReturnRequest(OrderProduct orderProduct, ProductReturnRequest productReturnRequest) {
        return validateOrderProductStatusForReturn(orderProduct) && orderProduct.getCount() >= orderProduct.getCancelAmount() + orderProduct.getCancelRequestAmount() + productReturnRequest.getReturnAmount() && productReturnRequest.getReturnAmount() > 0;
    }

    private boolean validateOrderProductStatusForReturn(OrderProduct orderProduct) {
        return orderProduct.getOrderStatus().equals(DELIVERED) || orderProduct.getOrderStatus().equals(RETURNED) || orderProduct.getOrderStatus().equals(DELIVERY_READY);
    }

    private void handleInvalidReturnRequest(OrderProduct orderProduct, ProductReturnRequest productReturnRequest) {
        if (orderProduct.getOrderStatus().equals(COMPLETED)) {
            throw new GlobalException("주문 완료 처리가 된 주문으로 반품 신청이 불가합니다.", CONFLICT);
        }
        if (!orderProduct.getOrderStatus().equals(DELIVERED) && !orderProduct.getOrderStatus().equals(RETURNED)) {
            throw new GlobalException("배송이 완료된 경우에만 반품 신청을 할 수 있습니다.", CONFLICT);
        }
        if (productReturnRequest.getReturnAmount() <= 0) {
            throw new GlobalException("유효하지 않은 반품 수량으로 인한 요청 처리 실패", CONFLICT);
        }
        if (orderProduct.getCount() - orderProduct.getCancelAmount() < orderProduct.getReturnAmount() + orderProduct.getReturnRequestAmount() + productReturnRequest.getReturnAmount()) {
            throw new GlobalException("반품 가능한 수량 보다 더 많은 수량에 대한 반품 요청으로 인한 처리 실패", CONFLICT);
        }
    }
}
