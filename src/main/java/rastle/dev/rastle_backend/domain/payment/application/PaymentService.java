package rastle.dev.rastle_backend.domain.payment.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import rastle.dev.rastle_backend.domain.coupon.exception.AlreadyUsedCouponException;
import rastle.dev.rastle_backend.domain.coupon.model.Coupon;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.*;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneWebHookRequest;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneWebHookResponse;
import rastle.dev.rastle_backend.domain.payment.exception.PaymentErrorException;
import rastle.dev.rastle_backend.domain.payment.exception.PaymentException;
import rastle.dev.rastle_backend.global.common.constants.PortOneStatusConstant;
import rastle.dev.rastle_backend.global.component.AsyncPortOneComponent;
import rastle.dev.rastle_backend.global.component.PortOneComponent;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static rastle.dev.rastle_backend.global.common.constants.PortOneMessageConstant.*;
import static rastle.dev.rastle_backend.global.common.constants.PortOneStatusConstant.*;
import static rastle.dev.rastle_backend.global.common.enums.CouponStatus.NOT_USED;
import static rastle.dev.rastle_backend.global.common.enums.CouponStatus.USED;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final CouponRepository couponRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PortOneComponent portOneComponent;
    private final ObjectMapper objectMapper;
    private final AsyncPortOneComponent asyncPortOneComponent;

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
    public URI verifyMobilePayment(String impUid, String merchantUid, String errorCode, String errorMsg)
            throws JsonProcessingException {
        try {
            if (errorCode != null) {
                throw new PaymentErrorException("결제 실패, errorMsg: " + errorMsg, errorCode);
            }
        } catch (PaymentErrorException e) {
            log.error("Payment error occurred: " + e.getMessage(), e);
            UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString("https://www.recordyslow.com/orderConfirm")
                .queryParam("errorMsg", objectMapper.writeValueAsString(e.getMessage()))
                .queryParam("errorCode", objectMapper.writeValueAsString(e.getErrorCode()))
                .queryParam("merchantUid", objectMapper.writeValueAsString(merchantUid));

            return URI.create(builder.toUriString());
        }

        PaymentResponse paymentResponse = portOneComponent.getPaymentData(impUid);
        String portOneMerchantUid = paymentResponse.getMerchantUID();
        log.info(merchantUid);

        if (!portOneMerchantUid.equals(merchantUid)) {
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
                    .fromUriString("https://www.recordyslow.com/orderConfirm")
                    // .fromUriString("http://localhost:3000/orderConfirm")
                    .queryParam("selectedProducts", objectMapper.writeValueAsString(selectedProducts))
                    .queryParam("orderInfo", objectMapper.writeValueAsString(paymentResponse.getMap()));

            return URI.create(builder.toUriString());
        } else {
            throw new PaymentException("결제 금액이 일치하지 않습니다.");
        }
    }

    private void handlePayment(PaymentResponse paymentResponse, OrderDetail orderDetail) {
        orderDetail.paid(paymentResponse);

        if (isCouponUsed(paymentResponse)) {
            useCoupon(paymentResponse, orderDetail);
        }

    }

    private boolean isCouponUsed(PaymentResponse paymentResponse) {
        return paymentResponse.getCouponId() != null;
    }

    private void useCoupon(PaymentResponse paymentData, OrderDetail orderDetail) {
        Coupon referenceById = couponRepository.getReferenceById(paymentData.getCouponId());
        referenceById.updateStatus(USED);
        orderDetail.getPayment().updateCouponAmount((long) referenceById.getDiscount());
    }


    @Transactional
    public PaymentPrepareResponse preparePayment(PaymentPrepareRequest paymentPrepareRequest) {
        String orderNumber = paymentPrepareRequest.getMerchant_uid();
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(Long.parseLong(orderNumber))
                .orElseThrow(() -> new PaymentException("주문 번호로 존재하는 주문이 없습니다. " + orderNumber));
        Long paymentPrice = orderDetail.getOrderPrice();
        if (paymentPrepareRequest.getCouponId() != null) {
            Coupon coupon = couponRepository.getReferenceById(paymentPrepareRequest.getCouponId());
            if (coupon.getCouponStatus() != NOT_USED) {
                throw new AlreadyUsedCouponException("이미 사용한 쿠폰");
            }
            paymentPrice -= coupon.getDiscount();
        }
        Long islandDeliveryPrice = paymentPrepareRequest.getIslandDeliveryPrice();
        if (islandDeliveryPrice == null) {
            islandDeliveryPrice = 0L;
        }
        paymentPrice += islandDeliveryPrice;
        paymentPrice += paymentPrepareRequest.getDeliveryPrice();
        orderDetail.getPayment().updatePaymentPrice(paymentPrice);
        asyncPortOneComponent.preparePayment(orderNumber, paymentPrice);
        return new PaymentPrepareResponse(orderNumber, paymentPrice);
    }

    @Transactional
    public PortOneWebHookResponse webhook(PortOneWebHookRequest webHookRequest) {

        switch (webHookRequest.getStatus()) {
            case PAID -> {
                asyncPortOneComponent.handlePayment(webHookRequest.getImp_uid());
                return PortOneWebHookResponse.builder()
                    .status(SUCCESS)
                    .message(SUCCESS_MSG)
                    .build();
            }
            case READY -> {
                asyncPortOneComponent.sendVbankEmail(webHookRequest.getImp_uid());
                return PortOneWebHookResponse.builder()
                    .status(VBANK_ISSUED)
                    .message(VBANK_ISSUED_MSG)
                    .build();
            }
            case PortOneStatusConstant.FAILED -> {
                asyncPortOneComponent.failedOrder(webHookRequest.getImp_uid());
                return PortOneWebHookResponse.builder()
                    .status(PortOneStatusConstant.FAILED)
                    .message(FAILED_MSG)
                    .build();
            }
            case PortOneStatusConstant.CANCELLED -> {
                asyncPortOneComponent.cancelledOrder(webHookRequest.getImp_uid());
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

    }
}
