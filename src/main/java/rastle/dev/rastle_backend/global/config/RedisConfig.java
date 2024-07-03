package rastle.dev.rastle_backend.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import rastle.dev.rastle_backend.global.cache.ObjectRedisTemplate;
import rastle.dev.rastle_backend.global.cache.StringRedisTemplate;
import rastle.dev.rastle_backend.global.common.constants.CacheConstant;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.username}")
    private String username;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${cache_host}")
    private String cacheHost;

    @Value("${cache_user}")
    private String cacheUser;

    @Value("${cache_password}")
    private String cachePassword;


    @Bean(name = "redisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setUsername(username);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean(name = "cacheRedisConnectionFactory")
    public RedisConnectionFactory cacheRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(cacheHost);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setUsername(cacheUser);
        redisStandaloneConfiguration.setPassword(cachePassword);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public StringRedisTemplate redisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        stringRedisTemplate.setDefaultSerializer(new StringRedisSerializer());
        return stringRedisTemplate;
    }

    @Bean
    public ObjectRedisTemplate cacheRedisTemplate() {
        ObjectRedisTemplate objectRedisTemplate = new ObjectRedisTemplate();
        objectRedisTemplate.setConnectionFactory(cacheRedisConnectionFactory());
        objectRedisTemplate.setKeySerializer(new StringRedisSerializer());
        objectRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return objectRedisTemplate;
    }

    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
            .entryTtl(Duration.ofMinutes(CacheConstant.EXPIRE_TIME)); // 캐쉬 저장 시간 15분 설정

        return RedisCacheManager
            .RedisCacheManagerBuilder
            .fromConnectionFactory(cacheRedisConnectionFactory())
            .cacheDefaults(redisCacheConfiguration)
            .build();
    }

}
