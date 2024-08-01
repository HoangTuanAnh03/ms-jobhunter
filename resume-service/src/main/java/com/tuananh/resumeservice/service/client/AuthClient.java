package com.tuananh.resumeservice.service.client;

import com.tuananh.resumeservice.config.AuthenticationRequestInterceptor;
import com.tuananh.resumeservice.dto.RestResponse;
import com.tuananh.resumeservice.dto.response.SimpInfoUserResponse;
import com.tuananh.resumeservice.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;


@FeignClient(name = "auth-service", url = "${app.services.auth}",
            configuration = AuthenticationRequestInterceptor.class)
public interface AuthClient {
    @GetMapping(value = "/users/my-info", produces = MediaType.APPLICATION_JSON_VALUE)
    RestResponse<UserResponse> getInfo();

    @GetMapping(value ="/users/fetchUserByIdIn", produces = MediaType.APPLICATION_JSON_VALUE)
    RestResponse<List<SimpInfoUserResponse>> fetchUserByIdIn(@RequestParam Set<String> ids);
}
