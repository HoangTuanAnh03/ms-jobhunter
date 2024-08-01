package com.tuananh.resumeservice.service.client;

import com.tuananh.resumeservice.config.AuthenticationRequestInterceptor;
import com.tuananh.resumeservice.dto.RestResponse;
import com.tuananh.resumeservice.entity.Company;
import com.tuananh.resumeservice.entity.Job;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;


@FeignClient(name = "company-service", url = "${app.services.company}",
        configuration = AuthenticationRequestInterceptor.class)
public interface CompanyClient {
    @GetMapping("/company/fetchByIdIn")
    RestResponse<List<Company>> fetchByIdIn(@RequestParam List<Long> ids);
}
