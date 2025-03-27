package chungha.diary.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;

@Profile("stress")
@Configuration
@EnableConfigurationProperties(RestTemplateProperties.class)
public class PooledRestTemplateConfig {

	private final RestTemplateProperties properties;

	public PooledRestTemplateConfig(RestTemplateProperties properties) {
		this.properties = properties;
	}

	// ✅ 1. 커넥션 매니저를 Bean으로 분리
	@Bean
	public PoolingHttpClientConnectionManager connectionManager() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(properties.getMaxTotalConnections());
		cm.setDefaultMaxPerRoute(properties.getMaxPerRoute());
		return cm;
	}

	// ✅ 2. 위 Bean을 사용해 HttpClient와 RestTemplate 구성
	@Bean(name = "pooledRestTemplate")
	public RestTemplate pooledRestTemplate(PoolingHttpClientConnectionManager connectionManager) {
		CloseableHttpClient httpClient = HttpClients.custom()
			.setConnectionManager(connectionManager)
			.build();

		HttpComponentsClientHttpRequestFactory requestFactory =
			new HttpComponentsClientHttpRequestFactory(httpClient);
		requestFactory.setConnectTimeout(properties.getConnectTimeout());
		requestFactory.setReadTimeout(properties.getReadTimeout());

		return new RestTemplate(new BufferingClientHttpRequestFactory(requestFactory));
	}

	// ✅ 3. 같은 connectionManager를 사용해 메트릭 등록
	@Bean
	public CommandLineRunner registerConnectionPoolMetrics(
		PoolingHttpClientConnectionManager connectionManager, MeterRegistry registry) {

		return args -> {
			Gauge.builder("httpclient.pool.active", connectionManager,
					cm -> cm.getTotalStats().getLeased())
				.description("Active HTTP connections")
				.register(registry);

			Gauge.builder("httpclient.pool.idle", connectionManager,
					cm -> cm.getTotalStats().getAvailable())
				.description("Idle HTTP connections")
				.register(registry);

			Gauge.builder("httpclient.pool.pending", connectionManager,
					cm -> cm.getTotalStats().getPending())
				.description("Pending connection requests")
				.register(registry);
		};
	}
}