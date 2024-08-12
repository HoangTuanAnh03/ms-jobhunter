package com.tuananh.resumeservice.service.client;

import com.tuananh.resumeservice.config.AuthenticationRequestInterceptor;
import com.tuananh.resumeservice.dto.ApiResponse;
import com.tuananh.resumeservice.entity.Company;
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
