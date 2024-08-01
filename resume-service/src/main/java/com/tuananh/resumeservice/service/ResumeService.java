package com.tuananh.resumeservice.service;

import com.tuananh.resumeservice.advice.exception.PermissionException;
import com.tuananh.resumeservice.dto.request.CreateResumeRequest;
import com.tuananh.resumeservice.dto.request.UpdateResumeRequest;
import com.tuananh.resumeservice.dto.response.ResultPaginationDTO;
import com.tuananh.resumeservice.dto.response.ResumeResponse;
import com.tuananh.resumeservice.entity.Resume;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ResumeService {

    /**
     * @param id - jobId
     * @return Resume Object on a given skillId
     */
    //
//    Job fetchById(long id);

    /**
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    //
    ResultPaginationDTO fetchMyResume(String id, Pageable pageable);

    /**
     * @param resumeRequest - CreateResumeRequest Object
     * @return Resume Object saved to database
     */
    //
    ResumeResponse create(CreateResumeRequest resumeRequest)throws Exception;

    /**
     * @param resumeRequest -UpdateResumeRequest Object
     * @return Resume Object updated to database
     */
    //
    ResumeResponse update(long id, UpdateResumeRequest resumeRequest);

    /**
     * @param id - Input jobId
     * @return boolean indicating if the delete of Resume details is successful or not
     */
    boolean delete(long id) ;

    ResultPaginationDTO fetchResumeByJob(long jobId, Pageable pageable) throws PermissionException;
}
