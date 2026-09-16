package COMP3011.assignment1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        String proxyHost = System.getProperty("https.proxyHost");
        String proxyPortStr = System.getProperty("https.proxyPort");

        HttpClient httpClient = HttpClient.create();

        if (proxyHost != null && proxyPortStr != null) {
            int proxyPort = Integer.parseInt(proxyPortStr);
            httpClient = httpClient.proxy(proxySpec -> proxySpec
                    .type(ProxyProvider.Proxy.HTTP)
                    .host(proxyHost)
                    .port(proxyPort));
        }

        return WebClient.builder()
                .baseUrl("https://api.openai.com")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}