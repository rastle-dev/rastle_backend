package rastle.dev.rastle_backend.global.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {
    @Bean(name = "deliveryTrackerTaskExecutor")
    public Executor deliveryTrackerPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);    // 기본 스레드 수
        threadPoolTaskExecutor.setMaxPoolSize(30);     // 최대 스레드 수
        threadPoolTaskExecutor.setThreadNamePrefix("DeliveryTrackerThread-");
        return threadPoolTaskExecutor;
    }

    @Bean(name = "portOneTaskExecutor")
    public Executor portOneWebhookPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);    // 기본 스레드 수
        threadPoolTaskExecutor.setMaxPoolSize(30);     // 최대 스레드 수
        threadPoolTaskExecutor.setThreadNamePrefix("PortOneWebhook-");
        return threadPoolTaskExecutor;
    }

    @Bean(name = "preparePaymentTaskExecutor")
    public Executor preparePaymentPool() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);    // 기본 스레드 수
        threadPoolTaskExecutor.setMaxPoolSize(30);     // 최대 스레드 수
        threadPoolTaskExecutor.setThreadNamePrefix("PreparePayment-");
        return threadPoolTaskExecutor;
    }

    @Bean(name = "cacheTaskExecutor")
    public Executor cacheTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);    // 기본 스레드 수
        threadPoolTaskExecutor.setMaxPoolSize(30);     // 최대 스레드 수
        threadPoolTaskExecutor.setThreadNamePrefix("CacheTask-");
        return threadPoolTaskExecutor;
    }
}