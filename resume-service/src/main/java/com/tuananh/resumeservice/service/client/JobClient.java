package com.tuananh.resumeservice.service.client;

import com.tuananh.resumeservice.config.AuthenticationRequestInterceptor;
import com.tuananh.resumeservice.dto.RestResponse;
import com.tuananh.resumeservice.dto.response.JobResponse;
import com.tuananh.resumeservice.entity.Job;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;


@FeignClient(name = "job-service", url = "${app.services.job}",
        configuration = AuthenticationRequestInterceptor.class,
        fallback = JobFallback.class)
public interface JobClient {
    @GetMapping(value = "/jobs/fetchById/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    RestResponse<Job> getById(@PathVariable("id") long id);

    @GetMapping("/jobs/fetchByIdIn")
    RestResponse<List<JobResponse>> fetchByIdIn(@RequestParam Set<Long> ids);
}
