package diary.config;


import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // 1. HttpClient 5.x용 커넥션 매니저 생성 및 설정
        // Apache HttpClient에서 연결 풀(Connection Pool)을 관리하는 역할을 합니다.
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(400);  // 전체 최대 커넥션 수
        connectionManager.setDefaultMaxPerRoute(300);  // 호스트별 최대 커넥션 수

        // 2. Apache HttpClient (5.x) 생성 (커넥션 풀 적용)
        // 실제 HTTP 요청을 수행하는 클라이언트 객체입니다.
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .build();

        // 3. 커스텀 HttpClient를 적용한 RequestFactory 생성
        // RestTemplate이 Apache HttpClient를 사용하여 HTTP 요청을 할 수 있도록 연결하는 어댑터 역할을 합니다.
        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(3000);  // 연결 타임아웃 (밀리초)
        requestFactory.setReadTimeout(6000);  // 읽기 타임아웃 (밀리초)

        // 4. RestTemplate 생성 후 반환
        return new RestTemplate(requestFactory);
    }
}