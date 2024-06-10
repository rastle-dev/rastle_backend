package rastle.dev.rastle_backend.global.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.cart.repository.mysql.CartProductRepository;
import rastle.dev.rastle_backend.domain.coupon.model.Coupon;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.payment.exception.PaymentException;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;

import static rastle.dev.rastle_backend.global.common.enums.CouponStatus.USED;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncComponent {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderProductRepository orderProductRepository;
    private final CouponRepository couponRepository;
    private final CartProductRepository cartProductRepository;
    private final PortOneComponent portOneComponent;
    private final MailComponent mailComponent;

    @Transactional
    @Async("asyncTaskExecutor")
    public void handlePayment(String impUid) {
        PaymentResponse paymentData = portOneComponent.getPaymentData(impUid);
        OrderDetail orderDetail = orderDetailRepository.findByOrderNumber(Long.parseLong(paymentData.getMerchantUID()))
                .orElseThrow(() -> new PaymentException("주문번호로 존재하는 주문이 DB에 존재하지 않는다"));

        orderDetail.paid(paymentData);

        if (isCouponUsed(paymentData)) {
            useCoupon(paymentData, orderDetail);
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


    @Async("asyncTaskExecutor")
    public void sendVbankEmail(String impUid) {
        PaymentResponse paymentData = portOneComponent.getPaymentData(impUid);
        mailComponent.sendBankIssueMessage(paymentData);

    }

    @Transactional
    @Async("asyncTaskExecutor")
    public void failedOrder(String impUid) {
        PaymentResponse paymentData = portOneComponent.getPaymentData(impUid);
        orderDetailRepository.updateOrderDetailFailed(Long.parseLong(paymentData.getMerchantUID()));
        orderProductRepository.updateOrderProductFailed(Long.parseLong(paymentData.getMerchantUID()));


    }

    @Transactional
    @Async("asyncTaskExecutor")
    public void cancelledOrder(String impUid) {
        PaymentResponse paymentData = portOneComponent.getPaymentData(impUid);
        orderDetailRepository.updateOrderDetailCancelled(Long.parseLong(paymentData.getMerchantUID()));
        orderProductRepository.updateOrderProductCancelled(Long.parseLong(paymentData.getMerchantUID()));
    }
}
