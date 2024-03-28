package rastle.dev.rastle_backend.domain.payment.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import rastle.dev.rastle_backend.domain.coupon.exception.CouponException;
import rastle.dev.rastle_backend.domain.coupon.model.Coupon;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.*;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneWebHookRequest;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneWebHookResponse;
import rastle.dev.rastle_backend.domain.payment.exception.PaymentException;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.global.common.constants.PortOneStatusConstant;
import rastle.dev.rastle_backend.global.component.MailComponent;
import rastle.dev.rastle_backend.global.component.PortOneComponent;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static rastle.dev.rastle_backend.global.common.constants.PortOneMessageConstant.*;
import static rastle.dev.rastle_backend.global.common.constants.PortOneStatusConstant.*;
import static rastle.dev.rastle_backend.global.common.enums.CouponStatus.NOT_USED;
import static rastle.dev.rastle_backend.global.common.enums.CouponStatus.USED;
import static rastle.dev.rastle_backend.global.common.enums.OrderStatus.CANCELLED;
import static rastle.dev.rastle_backend.global.common.enums.OrderStatus.FAILED;


@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final CouponRepository couponRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderProductRepository orderProductRepository;
    private final PortOneComponent portOneComponent;
    private final ObjectMapper objectMapper;
    private final MailComponent mailComponent;
    /*
    TODO: 결제 사후 검증 로직 검토
    TODO: 결제 사전 검증 로직 검토
    TODO: 웹훅 처리 로직 검토
     */

    @Transactional
    public PaymentVerificationResponse verifyPayment(PaymentVerificationRequest paymentVerificationRequest) {
        PaymentResponse paymentResponse = portOneComponent
            .getPaymentData(paymentVerificationRequest.getImp_uid());
        String merchantUid = paymentResponse.getMerchantUID();
        if (!merchantUid.equals(paymentVerificationRequest.getMerchant_uid())) {
            throw new PaymentException("포트원에서 전달받은 주문번호와, 브라우저에서 넘어온 주문번호가 다릅니다.");
        }
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(Long.parseLong(merchantUid))
            .orElseThrow(() -> new PaymentException("주문번호로 존재하는 주문이 DB에 존재하지 않는다"));

        if (orderDetail.getPayment().getPaymentPrice().equals(paymentResponse.getAmount())) {
            handlePayment(paymentResponse, orderDetail);
            return PaymentVerificationResponse.builder()
                .verified(true)
                .build();
        } else {
            return PaymentVerificationResponse.builder()
                .verified(false)
                .build();
        }
    }

    @Transactional
    public URI verifyMobilePayment(String impUid, String merchantUid, boolean impSuccess, String errorCode,
                                   String errorMsg)
        throws JsonProcessingException {
        if (!impSuccess) {
            throw new PaymentException("결제 실패, errorCode: " + errorCode + ", errorMsg: " + errorMsg);
        }

        PaymentResponse paymentResponse = portOneComponent.getPaymentData(impUid);
        String portoneMerchantUid = paymentResponse.getMerchantUID();
        log.info(merchantUid);

        if (!portoneMerchantUid.equals(merchantUid)) {
            throw new PaymentException("포트원에서 전달받은 주문번호와, 브라우저에서 넘어온 주문번호가 다릅니다.");
        }

        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(Long.parseLong(merchantUid))
            .orElseThrow(() -> new PaymentException("주문번호로 존재하는 주문이 DB에 존재하지 않습니다."));

        if (orderDetail.getPayment().getPaymentPrice().equals(paymentResponse.getAmount())) {
            handlePayment(paymentResponse, orderDetail);

            List<SelectedProductsDTO> selectedProducts = orderDetail.getOrderProduct().stream()
                .map(orderProduct -> SelectedProductsDTO.builder()
                    .title(orderProduct.getName())
                    .price(orderProduct.getPrice())
                    .color(orderProduct.getColor())
                    .size(orderProduct.getSize())
                    .count(orderProduct.getCount())
                    .mainThumbnailImage(orderProduct.getProduct().getMainThumbnailImage())
                    .build())
                .collect(Collectors.toList());
            UriComponentsBuilder builder = UriComponentsBuilder
                // .fromUriString("https://www.recordyslow.com/orderConfirmMobile")
                .fromUriString("http://localhost:3000/orderConfirm")
                .queryParam("selectedProducts", objectMapper.writeValueAsString(selectedProducts))
                .queryParam("orderInfo", objectMapper.writeValueAsString(paymentResponse.getMap()));

            return URI.create(builder.toUriString());
        } else {
            throw new PaymentException("결제 금액이 일치하지 않습니다.");
        }
    }

    private void handlePayment(PaymentResponse paymentResponse, OrderDetail orderDetail) {
        orderDetail.paid(paymentResponse);
        orderDetail.getPayment().paid(paymentResponse);
        orderDetail.getDelivery().paid(paymentResponse);

        if (paymentResponse.getCouponId() != null) {
            Coupon referenceById = couponRepository.getReferenceById(paymentResponse.getCouponId());
            referenceById.updateStatus(USED);
            orderDetail.getPayment().updateCouponAmount((long) referenceById.getDiscount());

        }
        List<OrderProduct> orderProducts = orderDetail.getOrderProduct();
        for (OrderProduct orderProduct : orderProducts) {
            ProductBase product = orderProduct.getProduct();
            product.incrementSoldCount();
        }
    }

    @Transactional
    public PaymentPrepareResponse preparePayment(PaymentPrepareRequest paymentPrepareRequest) {
        String orderNumber = paymentPrepareRequest.getMerchant_uid();
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(Long.parseLong(orderNumber))
            .orElseThrow(() -> new PaymentException("주문 번호로 존재하는 주문이 없습니다. " + orderNumber));
        Long orderPrice = orderDetail.getOrderPrice();
        Long paymentPrice = orderPrice;
        if (paymentPrepareRequest.getCouponId() != null) {
            Coupon coupon = couponRepository.getReferenceById(paymentPrepareRequest.getCouponId());
            if (coupon.getCouponStatus() != NOT_USED) {
                throw new CouponException("이미 사용한 쿠폰");
            }
            paymentPrice -= coupon.getDiscount();
        }
        Long islandDeliveryPrice = paymentPrepareRequest.getIslandDeliveryPrice();
        if (islandDeliveryPrice == null) {
            islandDeliveryPrice = 0L;
        }
        orderPrice += paymentPrepareRequest.getDeliveryPrice();
        orderPrice += islandDeliveryPrice;
        paymentPrice += paymentPrepareRequest.getDeliveryPrice();
        paymentPrice += islandDeliveryPrice;
        orderDetail.getPayment().updatePaymentPrice(paymentPrice);
        return portOneComponent.preparePayment(orderNumber, orderPrice);
    }

    @Transactional
    public PortOneWebHookResponse webhook(PortOneWebHookRequest webHookRequest) {
        PaymentResponse paymentResponse = portOneComponent.getPaymentData(webHookRequest.getImp_uid());
        String merchantUid = paymentResponse.getMerchantUID();
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(Long.parseLong(merchantUid))
            .orElseThrow(() -> new PaymentException("주문번호로 존재하는 주문이 DB에 존재하지 않는다"));

        if (orderDetail.getPayment().getPaymentPrice().equals(paymentResponse.getAmount())) {
            switch (webHookRequest.getStatus()) {
                case PAID -> {
                    if (paymentResponse.getCouponId() != null) {
                        Coupon referenceById = couponRepository.getReferenceById(paymentResponse.getCouponId());
                        referenceById.updateStatus(USED);
                    }
                    orderDetail.paid(paymentResponse);
                    return PortOneWebHookResponse.builder()
                        .status(SUCCESS)
                        .message(SUCCESS_MSG)
                        .build();
                }
                case READY -> {
                    mailComponent.sendBankIssueMessage(paymentResponse);
                    return PortOneWebHookResponse.builder()
                        .status(VBANK_ISSUED)
                        .message(VBANK_ISSUED_MSG)
                        .build();
                }
                case PortOneStatusConstant.FAILED -> {
                    orderDetail.updateOrderStatus(FAILED);
                    return PortOneWebHookResponse.builder()
                        .status(PortOneStatusConstant.FAILED)
                        .message(FAILED_MSG)
                        .build();
                }
                case PortOneStatusConstant.CANCELLED -> {
                    orderDetail.updateOrderStatus(CANCELLED);
                    return PortOneWebHookResponse.builder()
                        .status(PortOneStatusConstant.CANCELLED)
                        .message(CANCELLED_MSG)
                        .build();
                }
                default -> {
                    return PortOneWebHookResponse.builder()
                        .status("undefined")
                        .message("undefined")
                        .build();
                }

            }
        } else {
            return PortOneWebHookResponse.builder()
                .status(FORGERY)
                .message(FORGERY_MSG)
                .build();
        }
    }
}
