package rastle.dev.rastle_backend.domain.payment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderDetailRepository;
import rastle.dev.rastle_backend.domain.order.repository.mysql.OrderProductRepository;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO;
import rastle.dev.rastle_backend.domain.payment.repository.mysql.PaymentRepository;
import rastle.dev.rastle_backend.global.component.OrderNumberComponent;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderNumberComponent orderNumberComponent;
    @Transactional
    public PaymentDTO.PaymentVerificationResponse verifyPayment(PaymentDTO.PaymentVerificationRequest paymentVerificationRequest) {

        return null;
    }
}
