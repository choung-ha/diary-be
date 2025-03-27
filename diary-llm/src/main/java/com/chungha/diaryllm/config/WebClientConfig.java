package com.chungha.diaryllm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Profile("stress")
@Configuration
@EnableConfigurationProperties(WebClientProperties.class)
public class WebClientConfig {
	public WebClientConfig(WebClientProperties properties) {
		this.properties = properties;
	}

	private final WebClientProperties properties;

	@Bean
	public WebClient webClient(@Value("${spring.ai.openai.base-url}") String baseUrl) {
		ConnectionProvider provider = ConnectionProvider.builder("custom")
			.maxConnections(properties.getMaxConnections())
			.metrics(true)
			.build();
		HttpClient httpClient = HttpClient.create(provider);

		return WebClient.builder()
			.baseUrl(baseUrl)
			.clientConnector(new ReactorClientHttpConnector(httpClient))
			.build();
	}
}