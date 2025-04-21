package chungha.diary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class PageableConfig {

	@Bean
	public PageableHandlerMethodArgumentResolverCustomizer customizePageable() {
		return resolver -> {
			// 전역 pageable 설정
			resolver.setMaxPageSize(30); // max page size 제한
			resolver.setOneIndexedParameters(true); // page=1부터 시작하도록 설정
		};
	}
}
