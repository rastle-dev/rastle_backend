package rastle.dev.rastle_backend.global.cache;

import org.springframework.data.redis.core.RedisTemplate;
import rastle.dev.rastle_backend.global.common.constants.CacheConstant;

import java.util.concurrent.TimeUnit;

public class ObjectRedisTemplate extends RedisTemplate<String, Object> {

    public void save(String key, Object object) {
        super.opsForValue().set(key, object, CacheConstant.EXPIRE_TIME, TimeUnit.MINUTES);
    }
}
