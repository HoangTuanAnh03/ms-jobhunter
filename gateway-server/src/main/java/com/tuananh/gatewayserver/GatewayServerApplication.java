package com.tuananh.gatewayserver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayServerApplication {
    @Value("${app.api-prefix}")
    String apiPrefix;

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public RouteLocator RouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                    .path(apiPrefix + "/auth/**", apiPrefix + "/users/**")
                    .filters(f -> f
                                    .rewritePath(apiPrefix + "/auth/(?<segment>.*)", "/auth/${segment}")
                                    .rewritePath(apiPrefix + "/users/(?<segment>.*)", "/users/${segment}")
                        .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                        )
                    .uri("lb://AUTH-SERVICE"))
                .route(p -> p
                        .path(apiPrefix + "/company/**")
                        .filters(f -> f
                                .rewritePath(apiPrefix + "/company/(?<segment>.*)", "/company/${segment}")
                                .rewritePath(apiPrefix + "/company", "/company")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                        )
                        .uri("lb://COMPANY-SERVICE"))
                .route(p -> p
                        .path(apiPrefix + "/jobs/**", apiPrefix + "/skills/**")
                        .filters(f -> f
                                .rewritePath(apiPrefix + "/jobs/(?<segment>.*)", "/jobs/${segment}")
                                .rewritePath(apiPrefix + "/jobs", "/jobs")
                                .rewritePath(apiPrefix + "/skills/(?<segment>.*)", "/skills/${segment}")
                                .rewritePath(apiPrefix + "/skills", "/skills")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                        )
                        .uri("lb://JOB-SERVICE"))

                .route(p -> p
                        .path(apiPrefix + "/resume/**")
                        .filters(f -> f
                                .rewritePath(apiPrefix + "/resume/(?<segment>.*)", "/resume/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                        )
                        .uri("lb://RESUME-SERVICE"))


                .route(p -> p
                        .path(apiPrefix + "/files/**")
                        .filters(f -> f
                                .rewritePath(apiPrefix + "/files/(?<segment>.*)", "/files/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                        )
                        .uri("lb://FILE-SERVICE"))

                .build();
    }
}
