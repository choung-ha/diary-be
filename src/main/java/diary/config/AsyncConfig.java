package diary.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);   // 최소 스레드 50개
        executor.setMaxPoolSize(500);   // 최대 스레드 500개
        executor.setQueueCapacity(1000); // 큐에 1000개 대기 가능
        executor.setThreadNamePrefix("AsyncThread-");
        executor.setKeepAliveSeconds(60); // 사용되지 않는 스레드 60초 유지
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 큐가 꽉 차면 호출 스레드에서 실행
        executor.initialize();
        return executor;
    }
}