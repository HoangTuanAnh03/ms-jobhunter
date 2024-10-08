package com.tuananh.gatewayserver.configration;

import com.tuananh.gatewayserver.service.client.AuthClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfiguration {
    @Bean
    WebClient webClient(){
        return WebClient.builder()
                .baseUrl("http://localhost:8080/auth")
                .build();
    }

    @Bean
    AuthClient authClient(WebClient webClient){
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient)).build();

        return httpServiceProxyFactory.createClient(AuthClient.class);
    }
}
