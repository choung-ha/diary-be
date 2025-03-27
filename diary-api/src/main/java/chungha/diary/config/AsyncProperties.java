package chungha.diary.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "custom.async")
@Getter
@Setter
public class AsyncProperties {
	private int corePoolSize = 10;
	private int maxPoolSize = 100;
	private int queueCapacity = 500;
	private String threadNamePrefix = "AsyncThread-";
}