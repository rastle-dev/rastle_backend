package rastle.dev.rastle_backend.global.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentPrepareResponse;
import rastle.dev.rastle_backend.domain.payment.dto.PortOneDTO.PortOnePaymentResponse;
import rastle.dev.rastle_backend.global.component.dto.request.PortOnePaymentRequest;
import rastle.dev.rastle_backend.global.component.dto.request.PortOneTokenRequest;
import rastle.dev.rastle_backend.global.component.dto.response.PortOneTokenResponse;
import rastle.dev.rastle_backend.global.error.exception.port_one.PortOneException;

import java.util.Map;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class PortOneComponent {
    private final String apiKey = "0032172035760746";
    @Value("${port_one.secret}")
    private String apiSecret;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String BASE_URL = "https://api.iamport.kr";

    private static final String TOKEN_URL = "/users/getToken";
    private static final String PAYMENT_URL = "/payments/";
    private static final String PREPARE_URL = "/payments/prepare";

    public PortOnePaymentResponse getPaymentData(String impId, String merchantId) {
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        headers.set("Authorization", accessToken);

        HttpEntity request = new HttpEntity(headers);

        try {
            ResponseEntity<PortOnePaymentResponse> paymentResponse = restTemplate.exchange(
                    BASE_URL + PAYMENT_URL + impId,
                    GET,
                    request,
                    PortOnePaymentResponse.class
            );
            return paymentResponse.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            String encoded = convertString(message);
            log.info(encoded);
            throw new PortOneException(encoded);
        }
    }

    public PaymentPrepareResponse preparePayment(String merchantId, Long price) {
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        headers.set("Authorization", accessToken);

        PortOnePaymentRequest paymentRequest = new PortOnePaymentRequest(merchantId, price);

        HttpEntity request = new HttpEntity(paymentRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + PREPARE_URL, POST, request, String.class);
        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) responseMap.get("code");
            if (code != 0) {
                throw new PortOneException((String) responseMap.get("message"));
            }
            Map<String, Object> resultMap = (Map<String, Object>) responseMap.get("response");
            return new PaymentPrepareResponse((String) resultMap.get("merchant_uid"), (Integer) resultMap.get("amount"));

        } catch (JsonProcessingException e) {
            throw new PortOneException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);

        PortOneTokenRequest portOneTokenRequest = new PortOneTokenRequest(apiKey, apiSecret);

        log.info("imp_key " + apiKey);
        log.info("imp_secret " + apiSecret);
        try {
            ResponseEntity<PortOneTokenResponse> tokenResponse = restTemplate.postForEntity(BASE_URL + TOKEN_URL, new HttpEntity<>(objectMapper.writeValueAsString(portOneTokenRequest),  headers), PortOneTokenResponse.class);
            PortOneTokenResponse portOneTokenResponse = tokenResponse.getBody();
            log.info(portOneTokenResponse.getResponse().getAccess_token());
            return portOneTokenResponse.getResponse().getAccess_token();
        } catch (Exception e) {
            e.printStackTrace();
            String message = e.getMessage();
            String encoded = convertString(message);
            log.info(encoded);
            throw new PortOneException(e.getMessage());
        }

    }


    public static String convertString(String val) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < val.length(); i++) {
            if ('\\' == val.charAt(i) && 'u' == val.charAt(i + 1)) {
                Character r = (char) Integer.parseInt(val.substring(i + 2, i + 6), 16);
                sb.append(r);
                i += 5;
            } else {
                sb.append(val.charAt(i));
            }
        }
        return sb.toString();
    }


}
