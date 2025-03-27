package com.chungha.diaryllm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("stress")
@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient(@Value("${spring.ai.openai.base-url}") String baseUrl) {
		return WebClient.builder()
			.baseUrl(baseUrl)
			.build();
	}
}