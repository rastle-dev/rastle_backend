package rastle.dev.rastle_backend.domain.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.coupon.exception.CouponException;
import rastle.dev.rastle_backend.domain.coupon.model.Coupon;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentPrepareRequest;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentPrepareResponse;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentVerificationRequest;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentVerificationResponse;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneDTO.PortOnePaymentResponse;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneWebHookRequest;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneWebHookResponse;
import rastle.dev.rastle_backend.domain.payment.exception.PaymentException;
import rastle.dev.rastle_backend.domain.payment.repository.mysql.PaymentRepository;
import rastle.dev.rastle_backend.global.component.PortOneComponent;

import static rastle.dev.rastle_backend.global.common.constants.PortOneMessageConstant.*;
import static rastle.dev.rastle_backend.global.common.constants.PortOneStatusConstant.*;
import static rastle.dev.rastle_backend.global.common.enums.CouponStatus.NOT_USED;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final CouponRepository couponRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderProductRepository orderProductRepository;
    private final PortOneComponent portOneComponent;
    @Transactional
    public PaymentVerificationResponse verifyPayment(PaymentVerificationRequest paymentVerificationRequest) {
        PortOnePaymentResponse paymentResponse = portOneComponent.getPaymentData(paymentVerificationRequest.getImp_uid(), paymentVerificationRequest.getMerchant_uid());
        String merchantUid = paymentResponse.getResponse().getMerchant_uid();
        log.info(merchantUid);
        if (!merchantUid.equals(paymentVerificationRequest.getMerchant_uid())) {
            throw new PaymentException("포트원에서 전달받은 주문번호와, 브라우저에서 넘어온 주문번호가 다릅니다.");
        }
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(merchantUid).orElseThrow(() -> new PaymentException("주문번호로 존재하는 주문이 DB에 존재하지 않는다"));

        if (orderDetail.getPaymentPrice().equals(paymentResponse.getResponse().getAmount())) {
            orderDetail.paid(paymentResponse);
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
    public PaymentPrepareResponse preparePayment(PaymentPrepareRequest paymentPrepareRequest) {
        String orderNumber = paymentPrepareRequest.getMerchant_uid();
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(orderNumber).orElseThrow(() -> new PaymentException("주문 번호로 존재하는 주문이 없습니다. " + orderNumber));
        Long totalPrice = orderProductRepository.findOrderProductPriceSumByOrderNumber(orderNumber);
        Coupon coupon = couponRepository.getReferenceById(paymentPrepareRequest.getCouponId());
        if (coupon.getCouponStatus() != NOT_USED) {
            throw new CouponException("이미 사용한 쿠폰");
        }
        totalPrice -= coupon.getDiscount();
        totalPrice += paymentPrepareRequest.getDeliveryPrice();
        orderDetail.updatePaymentPrice(totalPrice);
        return portOneComponent.preparePayment(orderNumber, totalPrice);
    }
    @Transactional
    public PortOneWebHookResponse webhook(PortOneWebHookRequest webHookRequest) {
        PortOnePaymentResponse paymentResponse = portOneComponent.getPaymentData(webHookRequest.getImp_uid(), webHookRequest.getMerchant_uid());
        String merchantUid = paymentResponse.getResponse().getMerchant_uid();
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(merchantUid).orElseThrow(() -> new PaymentException("주문번호로 존재하는 주문이 DB에 존재하지 않는다"));

        if (orderDetail.getPaymentPrice().equals(paymentResponse.getResponse().getAmount())) {
            if (webHookRequest.getStatus().equals(PAID)) {
                orderDetail.paid(paymentResponse);
                return PortOneWebHookResponse.builder()
                    .status(SUCCESS)
                    .message(SUCCESS_MSG)
                    .build();
            } else if (webHookRequest.getStatus().equals(READY)) {
                // TODO : 가상 계좌 발급 메시지 보내면될듯?
                return PortOneWebHookResponse.builder()
                    .status(VBANK_ISSUED)
                    .message(VBANK_ISSUED_MSG)
                    .build();
            } else {
                return null;
            }
        } else {
            return PortOneWebHookResponse.builder()
                .status(FORGERY)
                .message(FORGERY_MSG)
                .build();
        }
    }
}
