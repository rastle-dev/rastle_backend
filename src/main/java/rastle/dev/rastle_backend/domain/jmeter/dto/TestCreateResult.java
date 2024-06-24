package rastle.dev.rastle_backend.domain.jmeter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rastle.dev.rastle_backend.domain.jmeter.model.TestDelivery;
import rastle.dev.rastle_backend.domain.jmeter.model.TestOrder;
import rastle.dev.rastle_backend.domain.jmeter.model.TestOrderProduct;
import rastle.dev.rastle_backend.domain.jmeter.model.TestPayment;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestCreateResult {
    TestDelivery testDelivery;
    TestOrder testOrder;
    TestPayment testPayment;
    TestOrderProduct testOrderProduct;
}
