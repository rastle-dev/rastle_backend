package rastle.dev.rastle_backend.global.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static rastle.dev.rastle_backend.global.common.constants.RedisConstant.PORT_ONE_ACCESS_DURATION;

@Component
@RequiredArgsConstructor
public class RedisCache {
    private final RedisTemplate<String, String> redisTemplate;

    public void save(String key, String data) {
        redisTemplate.opsForValue().set(key, data, PORT_ONE_ACCESS_DURATION, MILLISECONDS);

    }

    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
