package com.tuananh.gatewayserver.service.client;

import com.tuananh.gatewayserver.dto.request.IntrospectRequest;
import com.tuananh.gatewayserver.dto.response.IntrospectResponse;
import com.tuananh.gatewayserver.dto.response.RestResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

public interface AuthClient {
    @PostExchange(url = "/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<RestResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);
}