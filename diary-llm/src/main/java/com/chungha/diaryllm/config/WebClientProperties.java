package com.chungha.diaryllm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "custom.web-client")
@Getter
@Setter
public class WebClientProperties {
	private int maxConnections;
}
