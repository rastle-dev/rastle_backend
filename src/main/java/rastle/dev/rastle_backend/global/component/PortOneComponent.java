package rastle.dev.rastle_backend.global.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.domain.order.model.OrderProduct;
import rastle.dev.rastle_backend.global.cache.RedisCache;
import rastle.dev.rastle_backend.global.component.dto.request.PortOnePaymentCancelRequest;
import rastle.dev.rastle_backend.global.component.dto.request.PortOnePaymentRequest;
import rastle.dev.rastle_backend.global.component.dto.request.PortOneTokenRequest;
import rastle.dev.rastle_backend.global.component.dto.response.PaymentResponse;
import rastle.dev.rastle_backend.global.component.dto.response.PortOneTokenResponse;
import rastle.dev.rastle_backend.global.error.exception.port_one.PortOneException;

import java.util.Map;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpMethod.*;
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

    private ResponseEntity<String> getServerResponse(String url, HttpMethod method, HttpEntity request) {
        try {
            return restTemplate.exchange(url, method, request, String.class);
        } catch (RestClientException exception) {
            String accessToken = getAccessFromServer();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(singletonList(APPLICATION_JSON));
            headers.setContentType(APPLICATION_JSON);
            headers.set(AUTHORIZATION, accessToken);

            HttpEntity newRequest = new HttpEntity(request.getBody(), headers);
            return restTemplate.exchange(url, method, newRequest, String.class);

        }
    }

    public PaymentResponse getPaymentData(String impId) {
        HttpHeaders headers = setHeaders();
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> portOneResponse = getServerResponse(BASE_URL + PAYMENT_URL + impId, GET, request);
        try {
            Map<String, Object> responseMap = objectMapper.readValue(portOneResponse.getBody(), new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) responseMap.get(CODE);
            if (code != 0) {
                String encoded = convertString((String) responseMap.get(MESSAGE));
                log.error("exception during getting payment data - {}", encoded);
                throw new RuntimeException(encoded);
            }

            Map<String, Object> paymentResultmap = (Map<String, Object>) responseMap.get(RESPONSE);
            return new PaymentResponse(paymentResultmap, objectMapper);
        } catch (JsonProcessingException e) {
            throw new PortOneException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public PaymentResponse cancelPayment(String impId, OrderDetail orderDetail) {
        HttpHeaders headers = setHeaders();
        PortOnePaymentCancelRequest cancelRequest = PortOnePaymentCancelRequest.builder()
            .checksum(orderDetail.getPayment().getPaymentPrice() - orderDetail.getPayment().getCancelledSum())
            .amount(orderDetail.getPayment().getPaymentPrice() - orderDetail.getPayment().getCancelledSum())
            .merchant_uid(Long.toString(orderDetail.getOrderNumber()))
            .imp_uid(impId)
            .build();
        return getCancelResponse(headers, cancelRequest);
    }

    private Map<String, Object> getPreparePaymentResponseMap(HttpHeaders headers, String merchantId) throws JsonProcessingException {
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> serverResponse = getServerResponse(BASE_URL + PREPARE_URL + "/" + merchantId, GET, request);
        return objectMapper.readValue(serverResponse.getBody(), new TypeReference<Map<String, Object>>() {
        });
    }

    private PaymentResponse getCancelResponse(HttpHeaders headers, PortOnePaymentCancelRequest cancelRequest) {
        HttpEntity request = new HttpEntity(cancelRequest, headers);
        ResponseEntity<String> portOneResponse = getServerResponse(BASE_URL + CANCEL_URL, POST, request);
        try {
            Map<String, Object> responseMap = objectMapper.readValue(portOneResponse.getBody(), new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) responseMap.get(CODE);
            if (code != 0) {
                String encoded = convertString((String) responseMap.get(MESSAGE));
                log.error("exception during cancel portone request - {}", encoded);
                throw new RuntimeException(encoded);
            }

            Map<String, Object> paymentResultmap = (Map<String, Object>) responseMap.get(RESPONSE);
            return new PaymentResponse(paymentResultmap, objectMapper);
        } catch (JsonProcessingException e) {
            throw new PortOneException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public PaymentResponse cancelPayment(String impId, Long cancelAmount, OrderProduct orderProduct) {
        OrderDetail orderDetail = orderProduct.getOrderDetail();
        HttpHeaders headers = setHeaders();
        PortOnePaymentCancelRequest cancelRequest = PortOnePaymentCancelRequest.builder()
            .checksum(orderDetail.getPayment().getPaymentPrice() - orderDetail.getPayment().getCancelledSum())
            .amount(orderProduct.getPrice() * cancelAmount)
            .merchant_uid(Long.toString(orderDetail.getOrderNumber()))
            .imp_uid(impId)
            .build();
        return getCancelResponse(headers, cancelRequest);


    }

    public PaymentResponse returnPayment(String impId, OrderProduct orderProduct) {
        OrderDetail orderDetail = orderProduct.getOrderDetail();
        HttpHeaders headers = setHeaders();
        PortOnePaymentCancelRequest cancelRequest = PortOnePaymentCancelRequest.builder()
            .checksum(orderDetail.getPayment().getPaymentPrice() - orderDetail.getPayment().getCancelledSum())
            .amount(orderDetail.getPayment().getPaymentPrice() - orderDetail.getPayment().getCancelledSum() - 3000)
            .merchant_uid(Long.toString(orderDetail.getOrderNumber()))
            .imp_uid(impId)
            .build();
        return getCancelResponse(headers, cancelRequest);
    }

    public PaymentResponse returnPayment(String impId, Long returnRequestAmount, OrderProduct orderProduct) {
        OrderDetail orderDetail = orderProduct.getOrderDetail();
        HttpHeaders headers = setHeaders();
        PortOnePaymentCancelRequest cancelRequest = PortOnePaymentCancelRequest.builder()
            .checksum(orderDetail.getPayment().getPaymentPrice() - orderDetail.getPayment().getCancelledSum())
            .amount(orderProduct.getPrice() * returnRequestAmount - 3000)
            .merchant_uid(Long.toString(orderDetail.getOrderNumber()))
            .imp_uid(impId)
            .build();
        return getCancelResponse(headers, cancelRequest);
    }

    public Integer getCode(String merchantId) {
        HttpHeaders headers = setHeaders();
        try {
            Map<String, Object> responseMap = getPreparePaymentResponseMap(headers, merchantId);
            return (Integer) responseMap.get(CODE);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders setHeaders() {
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        headers.set(AUTHORIZATION, accessToken);
        return headers;
    }

    public void preparePayment(String merchantId, Long price) {
        Integer code = getCode(merchantId);
        if (code == -1) {
            preparePayment(merchantId, price, PUT);
        } else {
            preparePayment(merchantId, price, POST);
        }
    }

    private void preparePayment(String merchantId, Long price, HttpMethod method) {
        HttpHeaders headers = setHeaders();

        PortOnePaymentRequest paymentRequest = new PortOnePaymentRequest(merchantId, price);

        HttpEntity request = new HttpEntity(paymentRequest, headers);
        ResponseEntity<String> response = getServerResponse(BASE_URL + PREPARE_URL, method, request);
        try {
            Map<String, Object> responseMap = objectMapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
            });
            Integer code = (Integer) responseMap.get(CODE);
            if (code != 0) {
                String encoded = convertString((String) responseMap.get(MESSAGE));
                log.error("exception during payment prepare portone request - {}", encoded);
                throw new PortOneException(encoded);
            }
            log.info("{} payment prepare success", merchantId);

        } catch (Exception e) {
            throw new PortOneException(e.getMessage());
        }
    }

    private String getAccessToken() {
        if (redisCache.get(PORT_ONE_ACCESS) != null) {
            return redisCache.get(PORT_ONE_ACCESS);
        } else {
            return getAccessFromServer();
        }
    }

    private String getAccessFromServer() {
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
