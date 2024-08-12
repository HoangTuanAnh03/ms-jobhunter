package com.tuananh.jobservice.service.client;

import com.tuananh.jobservice.config.AuthenticationRequestInterceptor;
import com.tuananh.jobservice.dto.ApiResponse;
import com.tuananh.jobservice.dto.response.Company;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "company-service", url = "${app.services.company}",
        configuration = AuthenticationRequestInterceptor.class)
public interface CompanyClient {
    @GetMapping("/company/fetchByIdIn")
    ApiResponse<List<Company>> fetchByIdIn(@RequestParam List<Long> ids);
}
