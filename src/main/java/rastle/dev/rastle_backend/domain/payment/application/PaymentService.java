package rastle.dev.rastle_backend.domain.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentVerificationRequest;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentVerificationResponse;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneDTO;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneDTO.PortOnePaymentResponse;
import rastle.dev.rastle_backend.domain.payment.exception.PaymentException;
import rastle.dev.rastle_backend.domain.payment.repository.mysql.PaymentRepository;
import rastle.dev.rastle_backend.global.component.OrderNumberComponent;
import rastle.dev.rastle_backend.global.component.PortOneComponent;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderNumberComponent orderNumberComponent;
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
        List<OrderProduct> orderProducts = orderDetail.getOrderProduct();
        Long paymentSum = 0L;
        for (OrderProduct orderProduct : orderProducts) {
            paymentSum += orderProduct.getTotalPrice();
        }
        if (paymentSum.equals(paymentResponse.getResponse().getAmount())) {
            return PaymentVerificationResponse.builder()
                    .verified(true)
                    .build();
        } else {
            return PaymentVerificationResponse.builder()
                    .verified(false)
                    .build();
        }

    }
}
