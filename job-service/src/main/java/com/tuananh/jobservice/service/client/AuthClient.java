package com.tuananh.jobservice.service.client;

import com.tuananh.jobservice.config.AuthenticationRequestInterceptor;
import com.tuananh.jobservice.dto.RestResponse;
import com.tuananh.jobservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@FeignClient(name = "auth-service", url = "${app.services.auth}",
            configuration = AuthenticationRequestInterceptor.class)
public interface AuthClient {
    @GetMapping(value = "/users/my-info", produces = MediaType.APPLICATION_JSON_VALUE)
    RestResponse<UserResponse> getInfo();

    @PutMapping(value = "/users/updateHR/{companyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    RestResponse<UserResponse> updateHR(@PathVariable(name = "companyId") long companyId);
}
