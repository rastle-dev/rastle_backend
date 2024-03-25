package rastle.dev.rastle_backend.global.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.payment.dto.PaymentDTO.PaymentPrepareResponse;
import rastle.dev.rastle_backend.global.cache.RedisCache;
import rastle.dev.rastle_backend.global.component.dto.request.PortOnePaymentCancelRequest;
import rastle.dev.rastle_backend.global.component.dto.request.PortOnePaymentRequest;
import rastle.dev.rastle_backend.global.component.dto.request.PortOneTokenRequest;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;
import rastle.dev.rastle_backend.global.component.dto.response.PortOneTokenResponse;
import rastle.dev.rastle_backend.global.error.exception.port_one.PortOneException;

import java.util.Map;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static rastle.dev.rastle_backend.global.common.constants.PortOneApiConstant.*;
import static rastle.dev.rastle_backend.global.common.constants.RedisConstant.PORT_ONE_ACCESS;

@Component
@Slf4j
@RequiredArgsConstructor
public class PortOneComponent {
    @Value("${port_one.secret}")
    private String API_SECRET;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RedisCache redisCache;

    public PaymentResponse getPaymentData(String impId) {
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        headers.set(AUTHORIZATION, accessToken);

        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> portoneResponse = restTemplate.exchange(BASE_URL + PAYMENT_URL + impId,
            GET,
            request,
            String.class);
        try {
            Map<String, Object> responseMap = objectMapper.readValue(portoneResponse.getBody(), new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) responseMap.get(CODE);
            if (code != 0) {
                throw new RuntimeException((String) responseMap.get(MESSAGE));
            }

            Map<String, Object> paymentResultmap = (Map<String, Object>) responseMap.get(RESPONSE);
            return new PaymentResponse(paymentResultmap, objectMapper);
        } catch (JsonProcessingException e) {
            throw new PortOneException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public PaymentResponse cancelPayment(String impId, Long cancelAmount, OrderDetail orderDetail) {
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        headers.set(AUTHORIZATION, accessToken);
        PortOnePaymentCancelRequest cancelRequest = PortOnePaymentCancelRequest.builder()
            .checksum(orderDetail.getPayment().getPaymentPrice() - orderDetail.getPayment().getCancelledSum())
            .amount(cancelAmount)
            .merchant_uid(Long.toString(orderDetail.getOrderNumber()))
            .imp_uid(impId)
            .build();
        HttpEntity request = new HttpEntity(cancelRequest, headers);
        ResponseEntity<String> portoneResponse = restTemplate.exchange(BASE_URL + CANCEL_URL,
            POST,
            request,
            String.class);
        try {
            Map<String, Object> responseMap = objectMapper.readValue(portoneResponse.getBody(), new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) responseMap.get(CODE);
            if (code != 0) {
                throw new RuntimeException((String) responseMap.get(MESSAGE));
            }

            Map<String, Object> paymentResultmap = (Map<String, Object>) responseMap.get(RESPONSE);
            return new PaymentResponse(paymentResultmap, objectMapper);
        } catch (JsonProcessingException e) {
            throw new PortOneException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }


    public PaymentPrepareResponse preparePayment(String merchantId, Long price) {
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        headers.set(AUTHORIZATION, accessToken);

        PortOnePaymentRequest paymentRequest = new PortOnePaymentRequest(merchantId, price);

        HttpEntity request = new HttpEntity(paymentRequest, headers);
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL + PREPARE_URL, POST, request, String.class);
        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) responseMap.get(CODE);
            if (code != 0) {
                throw new PortOneException((String) responseMap.get(MESSAGE));
            }
            Map<String, Object> resultMap = (Map<String, Object>) responseMap.get(RESPONSE);
            return new PaymentPrepareResponse((String) resultMap.get(MERCHANT_UID), (Integer) resultMap.get(AMOUNT));

        } catch (JsonProcessingException e) {
            throw new PortOneException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private String getAccessToken() {
        if (redisCache.get(PORT_ONE_ACCESS) != null) {
            return redisCache.get(PORT_ONE_ACCESS);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(singletonList(APPLICATION_JSON));
            headers.setContentType(APPLICATION_JSON);

            PortOneTokenRequest portOneTokenRequest = new PortOneTokenRequest(API_KEY, API_SECRET);

            try {
                ResponseEntity<PortOneTokenResponse> tokenResponse = restTemplate.postForEntity(BASE_URL + TOKEN_URL, new HttpEntity<>(objectMapper.writeValueAsString(portOneTokenRequest), headers), PortOneTokenResponse.class);
                PortOneTokenResponse portOneTokenResponse = tokenResponse.getBody();

                String accessToken = portOneTokenResponse.getResponse().getAccess_token();
                redisCache.save(PORT_ONE_ACCESS, accessToken);
                return accessToken;
            } catch (Exception e) {
                String message = e.getMessage();
                String encoded = convertString(message);
                log.info(encoded);
                throw new PortOneException(encoded);
            }
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
