package com.tuananh.gatewayserver.service;

import com.tuananh.gatewayserver.dto.request.IntrospectRequest;
import com.tuananh.gatewayserver.dto.response.IntrospectResponse;
import com.tuananh.gatewayserver.dto.response.RestResponse;
import com.tuananh.gatewayserver.service.client.AuthClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    AuthClient authClient;

    public Mono<RestResponse<IntrospectResponse>> introspect(String token){
        return authClient.introspect(IntrospectRequest.builder()
                .token(token)
                .build());
    }
}