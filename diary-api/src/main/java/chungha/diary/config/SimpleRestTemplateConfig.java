package chungha.diary.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Profile("stress")
@Configuration
@EnableConfigurationProperties(RestTemplateProperties.class)
public class SimpleRestTemplateConfig {

	private final RestTemplateProperties properties;

	public SimpleRestTemplateConfig(RestTemplateProperties properties) {
		this.properties = properties;
	}

	@Bean(name = "simpleRestTemplate")
	public RestTemplate simpleRestTemplate() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(properties.getConnectTimeout());
		factory.setReadTimeout(properties.getReadTimeout());

		return new RestTemplate(factory);
	}
}