package com.tuananh.authservice.service.client;

import com.tuananh.authservice.dto.RestResponse;
import com.tuananh.authservice.dto.response.IntegrateInfoCompanyRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "company-service", url = "${app.services.company}")
public interface CompanyClient {
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    RestResponse<IntegrateInfoCompanyRes> getById(@PathVariable("id") long id);
}
