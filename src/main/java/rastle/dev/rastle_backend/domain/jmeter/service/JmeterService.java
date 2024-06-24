package rastle.dev.rastle_backend.domain.jmeter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.jmeter.dto.TestCreateResult;
import rastle.dev.rastle_backend.domain.jmeter.model.TestDelivery;
import rastle.dev.rastle_backend.domain.jmeter.model.TestOrder;
import rastle.dev.rastle_backend.domain.jmeter.model.TestOrderProduct;
import rastle.dev.rastle_backend.domain.jmeter.model.TestPayment;
import rastle.dev.rastle_backend.domain.jmeter.repository.mysql.TestDeliveryRepository;
import rastle.dev.rastle_backend.domain.jmeter.repository.mysql.TestOrderProductRepository;
import rastle.dev.rastle_backend.domain.jmeter.repository.mysql.TestOrderRepository;
import rastle.dev.rastle_backend.domain.jmeter.repository.mysql.TestPaymentRepository;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@Service
@RequiredArgsConstructor
public class JmeterService {

    private final TestDeliveryRepository deliveryRepository;
    private final TestOrderRepository orderRepository;
    private final TestOrderProductRepository orderProductRepository;
    private final TestPaymentRepository paymentRepository;
    @Transactional
    public ServerResponse<?> createTestOrder() {
        TestOrder testOrder = TestOrder.newOrder();
        TestDelivery testDelivery = TestDelivery.newDelivery();
        TestOrderProduct testOrderProduct = TestOrderProduct.newOrderProduct();
        TestPayment testPayment = TestPayment.newPayment();
        orderRepository.save(testOrder);
        orderProductRepository.save(testOrderProduct);
        paymentRepository.save(testPayment);
        deliveryRepository.save(testDelivery);
        return new ServerResponse<>(new TestCreateResult(testDelivery, testOrder, testPayment, testOrderProduct));
    }

}

