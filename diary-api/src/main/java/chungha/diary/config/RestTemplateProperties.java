package chungha.diary.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "custom.rest-template")
@Getter
@Setter
public class RestTemplateProperties {
	private int connectTimeout;
	private int readTimeout;
	private int maxTotalConnections;
	private int maxPerRoute;
}