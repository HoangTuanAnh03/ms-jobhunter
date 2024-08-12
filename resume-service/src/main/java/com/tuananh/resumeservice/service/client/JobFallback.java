package com.tuananh.resumeservice.service.client;


import com.tuananh.resumeservice.dto.ApiResponse;
import com.tuananh.resumeservice.dto.response.JobResponse;
import com.tuananh.resumeservice.entity.Job;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class JobFallback implements JobClient{

    /**
     * @param id
     * @return
     */
    @Override
    public ApiResponse<Job> getById(long id) {
        return null;
    }

    /**
     * @param ids
     * @return
     */
    @Override
    public ApiResponse<List<JobResponse>> fetchByIdIn(Set<Long> ids) {
        return null;
    }
}
