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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rastle.dev.rastle_backend.global.cache.RedisCache;
import rastle.dev.rastle_backend.global.common.enums.DeliveryTrackerStatus;
import rastle.dev.rastle_backend.global.component.dto.DeliveryTrackerResponse;
import rastle.dev.rastle_backend.global.component.dto.request.GetDeliveryStatusRequest;
import rastle.dev.rastle_backend.global.component.dto.request.WebHookRegisterRequest;

import java.time.LocalDateTime;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static rastle.dev.rastle_backend.global.common.constants.DeliveryTrackerConstant.*;
import static rastle.dev.rastle_backend.global.common.constants.PortOneApiConstant.AUTHORIZATION;
import static rastle.dev.rastle_backend.global.common.constants.RedisConstant.DELIVER_TRACKER_ACCESS;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeliveryTracker {
    @Value("${delivery_tracker.id}")
    private String id;
    @Value("${delivery_tracker.secret}")
    private String secret;
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

    public DeliveryTrackerStatus getDeliveryStatus(String trackingNumber) {
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        headers.set(AUTHORIZATION, accessToken);
        GetDeliveryStatusRequest deliveryStatusRequest = GetDeliveryStatusRequest.builder()
            .query(SEARCH_TRACKING_NUMBER_QUERY)
            .carrierId(CARRIER_ID)
            .trackingNumber(trackingNumber)
            .build();

        HttpEntity request = new HttpEntity(deliveryStatusRequest, headers);
        ResponseEntity<String> serverResponse = getServerResponse(BASE_URL + GRAPH_QL, POST, request);
        try {
            Map<String, Object> responseMap = objectMapper.readValue(serverResponse.getBody(), new TypeReference<Map<String, Object>>() {
            });
            DeliveryTrackerResponse trackerResponse = new DeliveryTrack"erResponse(responseMap, objectMapper);
            return trackerResponse.getLastEventStatus();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public void registerWebHook(String trackingNumber) {
        String accessToken = getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        headers.set(AUTHORIZATION, accessToken);
        WebHookRegisterRequest webHookRegisterRequest = WebHookRegisterRequest.builder()
            .callbackUrl(CALLBACK_URL)
            .carrierId(CARRIER_ID)
            .expirationTime(LocalDateTime.now().plusDays(2))
            .trackingNumber(trackingNumber)
            .query(REGISTER_WEB_HOOK_QUERY)
            .build();

        HttpEntity request = new HttpEntity(webHookRegisterRequest, headers);
        ResponseEntity<String> serverResponse = getServerResponse(BASE_URL + GRAPH_QL, POST, request);
        try {
            Map<String, Object> responseMap = objectMapper.readValue(serverResponse.getBody(), new TypeReference<Map<String, Object>>() {
            });
            Map<String, Object> responseData = (Map<String, Object>) responseMap.get("data");
            if (!(boolean) responseData.get("registerTrackWebhook")) {
                throw new RuntimeException("webhook not registered successfully");
            }
            log.info("{} delivery tracker webhook registered", trackingNumber);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    private MultiValueMap<String, String> initParams() {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("grant_type", GRANT_TYPE);
        multiValueMap.add("client_id", id);
        multiValueMap.add("client_secret", secret);
        return multiValueMap;
    }

    private String getAccessToken() {
        if (redisCache.get(DELIVER_TRACKER_ACCESS) != null) {
            return redisCache.get(DELIVER_TRACKER_ACCESS);
        } else {
            return getAccessFromServer();

        }

    }

    private String getAccessFromServer() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(singletonList(APPLICATION_JSON));
        headers.setContentType(APPLICATION_FORM_URLENCODED);
        HttpEntity request = new HttpEntity(initParams(), headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(TOKEN_URL, POST, request, String.class);
        try {
            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {
            });
            if (responseMap.containsKey("access_token")) {
                String accessToken = (String) responseMap.get("access_token");
                accessToken = "Bearer " + accessToken;
                redisCache.save(DELIVER_TRACKER_ACCESS, accessToken);
                return accessToken;
            } else {
                throw new RuntimeException("cant get access token from delivery tracker api server");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
