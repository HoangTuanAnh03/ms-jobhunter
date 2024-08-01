package com.tuananh.jobservice.service;

import com.tuananh.jobservice.advice.exception.PermissionException;
import com.tuananh.jobservice.dto.request.CreateJobRequest;
import com.tuananh.jobservice.dto.request.UpdateJobRequest;
import com.tuananh.jobservice.dto.response.JobResponse;
import com.tuananh.jobservice.dto.response.ResultPaginationDTO;
import com.tuananh.jobservice.entity.Job;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

public interface JobService {

    /**
     * @param id - jobId
     * @return Job Object on a given skillId
     */
    //
    Job fetchById(long id);

    /**
     * @param spec     - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    //
    ResultPaginationDTO fetchPagination(Specification<Job> spec, Pageable pageable);

    /**
     * @param jobRequest - CreateJobRequest Object
     * @return Job Object saved to database
     */
    //
    Job create(CreateJobRequest jobRequest);

    /**
     * @param jobRequest -UpdateJobRequest Object
     * @return Job Object updated to database
     */
    //
    Job update(long id, UpdateJobRequest jobRequest) throws PermissionException;

    /**
     * @param id - Input jobId
     * @return boolean indicating if the delete of Job details is successful or not
     */
    //
    boolean delete(long id) throws PermissionException;

    List<JobResponse> fetchByIdIn(Set<Long> listId);
}
