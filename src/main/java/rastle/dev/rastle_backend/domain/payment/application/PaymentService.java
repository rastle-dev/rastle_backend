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
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.*;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneDTO.PortOnePaymentResponse;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneDTO.PortOnePaymentResponse.CustomData;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneWebHookRequest;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneWebHookResponse;
import rastle.dev.rastle_backend.domain.payment.exception.PaymentException;
import rastle.dev.rastle_backend.global.common.constants.PortOneStatusConstant;
import rastle.dev.rastle_backend.global.common.enums.PaymentStatus;
import rastle.dev.rastle_backend.global.component.MailComponent;
import rastle.dev.rastle_backend.global.component.PortOneComponent;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static rastle.dev.rastle_backend.global.common.constants.PortOneMessageConstant.*;
import static rastle.dev.rastle_backend.global.common.constants.PortOneStatusConstant.*;
import static rastle.dev.rastle_backend.global.common.enums.CouponStatus.NOT_USED;
import static rastle.dev.rastle_backend.global.common.enums.CouponStatus.USED;
import static rastle.dev.rastle_backend.global.common.enums.PaymentStatus.CANCELED;

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

    @Transactional
    public PaymentVerificationResponse verifyPayment(PaymentVerificationRequest paymentVerificationRequest) {
        PortOnePaymentResponse paymentResponse = portOneComponent
                .getPaymentData(paymentVerificationRequest.getImp_uid(), paymentVerificationRequest.getMerchant_uid());
        String merchantUid = paymentResponse.getResponse().getMerchant_uid();
        log.info(merchantUid);
        if (!merchantUid.equals(paymentVerificationRequest.getMerchant_uid())) {
            throw new PaymentException("포트원에서 전달받은 주문번호와, 브라우저에서 넘어온 주문번호가 다릅니다.");
        }
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(merchantUid)
                .orElseThrow(() -> new PaymentException("주문번호로 존재하는 주문이 DB에 존재하지 않는다"));

        if (orderDetail.getPaymentPrice().equals(paymentResponse.getResponse().getAmount())) {
            // TODO: 포트원 응답에서 쿠폰 관련 값 받아서 쿠폰 사용 처리 해줘야함
            orderDetail.paid(paymentResponse);
            try {
                CustomData customData = objectMapper.readValue(paymentResponse.getResponse().getCustom_data(), CustomData.class);
                if (customData.getCouponId() != null) {
                    Coupon referenceById = couponRepository.getReferenceById(customData.getCouponId());
                    referenceById.updateStatus(USED);
                }
                orderDetail.updateDeliveryPrice(customData.getDeliveryPrice());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
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

        PortOnePaymentResponse paymentResponse = portOneComponent.getPaymentData(impUid, merchantUid);
        String portoneMerchantUid = paymentResponse.getResponse().getMerchant_uid();
        log.info(merchantUid);

        if (!portoneMerchantUid.equals(merchantUid)) {
            throw new PaymentException("포트원에서 전달받은 주문번호와, 브라우저에서 넘어온 주문번호가 다릅니다.");
        }

        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(merchantUid)
                .orElseThrow(() -> new PaymentException("주문번호로 존재하는 주문이 DB에 존재하지 않습니다."));

        if (orderDetail.getPaymentPrice().equals(paymentResponse.getResponse().getAmount())) {
            orderDetail.paid(paymentResponse);

            List<SelectedProductsDTO> selectedProducts = orderDetail.getOrderProduct().stream()
                    .map(orderProduct -> SelectedProductsDTO.builder()
                            .title(orderProduct.getName())
                            .price(orderProduct.getTotalPrice())
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
                    .queryParam("orderInfo", objectMapper.writeValueAsString(paymentResponse.getResponse()));

            return URI.create(builder.toUriString());
        } else {
            throw new PaymentException("결제 금액이 일치하지 않습니다.");
        }
    }

    @Transactional
    public PaymentPrepareResponse preparePayment(PaymentPrepareRequest paymentPrepareRequest) {
        String orderNumber = paymentPrepareRequest.getMerchant_uid();
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new PaymentException("주문 번호로 존재하는 주문이 없습니다. " + orderNumber));
        Long totalPrice = orderProductRepository.findOrderProductPriceSumByOrderNumber(orderNumber);
        if (paymentPrepareRequest.getCouponId() != null) {
            Coupon coupon = couponRepository.getReferenceById(paymentPrepareRequest.getCouponId());
            if (coupon.getCouponStatus() != NOT_USED) {
                throw new CouponException("이미 사용한 쿠폰");
            }
            totalPrice -= coupon.getDiscount();
        }
        totalPrice += paymentPrepareRequest.getDeliveryPrice();
        orderDetail.updatePaymentPrice(totalPrice);
        return portOneComponent.preparePayment(orderNumber, totalPrice);
    }

    @Transactional
    public PortOneWebHookResponse webhook(PortOneWebHookRequest webHookRequest) {
        PortOnePaymentResponse paymentResponse = portOneComponent.getPaymentData(webHookRequest.getImp_uid(),
                webHookRequest.getMerchant_uid());
        String merchantUid = paymentResponse.getResponse().getMerchant_uid();
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(merchantUid)
                .orElseThrow(() -> new PaymentException("주문번호로 존재하는 주문이 DB에 존재하지 않는다"));

        if (orderDetail.getPaymentPrice().equals(paymentResponse.getResponse().getAmount())) {
            switch (webHookRequest.getStatus()) {
                case PAID -> {
                    CustomData customData = null;
                    try {
                        customData = objectMapper.readValue(paymentResponse.getResponse().getCustom_data(), CustomData.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    if (customData.getCouponId() != null) {
                        Coupon referenceById = couponRepository.getReferenceById(customData.getCouponId());
                        referenceById.updateStatus(USED);
                    }
                    orderDetail.updateDeliveryPrice(customData.getDeliveryPrice());
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
                    orderDetail.updatePaymentStatus(PaymentStatus.FAILED);
                    return PortOneWebHookResponse.builder()
                        .status(PortOneStatusConstant.FAILED)
                        .message(FAILED_MSG)
                        .build();
                }
                case CANCELLED -> {
                    orderDetail.updatePaymentStatus(CANCELED);
                    return PortOneWebHookResponse.builder()
                        .status(CANCELLED)
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
