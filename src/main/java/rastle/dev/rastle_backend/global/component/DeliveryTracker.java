package rastle.dev.rastle_backend.global.component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import rastle.dev.rastle_backend.global.cache.RedisCache;

import java.util.Map;

import static java.util.Collections.singletonList;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static rastle.dev.rastle_backend.global.common.constants.DeliveryTrackerConstant.*;
import static rastle.dev.rastle_backend.global.common.constants.RedisConstant.DELIVER_TRACKER_ACCESS;

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

    public String registerWebHook(String trackingNumber) {
        String access = getAccessToken();
        return access;
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
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(singletonList(APPLICATION_JSON));
            headers.setContentType(APPLICATION_FORM_URLENCODED);
            HttpEntity request = new HttpEntity(initParams(), headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(BASE_URL + TOKEN_URI, POST, request, String.class);
            try {
                Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {
                });
                if (responseMap.containsKey("access_token")) {
                    String accessToken = (String) responseMap.get("access_token");
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


}
