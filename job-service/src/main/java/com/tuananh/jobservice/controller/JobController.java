package com.tuananh.jobservice.controller;


import com.tuananh.jobservice.advice.exception.PermissionException;
import com.tuananh.jobservice.dto.ApiResponse;
import com.tuananh.jobservice.dto.request.CreateJobRequest;
import com.tuananh.jobservice.dto.request.UpdateJobRequest;
import com.tuananh.jobservice.dto.response.JobResponse;
import com.tuananh.jobservice.dto.response.ResultPaginationDTO;
import com.tuananh.jobservice.entity.Job;
import com.tuananh.jobservice.service.JobService;
import com.tuananh.jobservice.util.constant.SkillConstants;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


/**
 * @author Tuan Anh Hoang
 */

@Tag(
        name = "CRUD REST APIs for Jobs in JobHunter",
        description = "CRUD REST APIs in JobHunter to CREATE, UPDATE, FETCH AND DELETE job details"
)
@RestController
@RequestMapping("/jobs")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobController {
    JobService jobService;

    @GetMapping("/fetchById/{id}")
    public ApiResponse<Job> getById(@PathVariable("id") long id) {
        return ApiResponse.<Job>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch job by id")
                .data(jobService.fetchById(id))
                .build();
    }

    @GetMapping("/fetchByIdIn")
    public ApiResponse<List<JobResponse>> fetchByIdIn(@RequestParam Set<Long> ids) {
        return ApiResponse.<List<JobResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch job by ids")
                .data(jobService.fetchByIdIn(ids))
                .build();
    }

    @GetMapping("/pagination")
    public ApiResponse<ResultPaginationDTO> fetchPagination(
            @Filter Specification<Job> spec, Pageable pageable) {

        return ApiResponse.<ResultPaginationDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch skills")
                .data(this.jobService.fetchPagination(spec, pageable))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<Job> create(@Valid @RequestBody CreateJobRequest jobRequest) {

        return ApiResponse.<Job>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create a job")
                .data(this.jobService.create(jobRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Job> update(@PathVariable("id") long id, @Valid @RequestBody UpdateJobRequest jobRequest)
            throws PermissionException {
        return ApiResponse.<Job>builder()
                .code(HttpStatus.OK.value())
                .message("Update a job")
                .data(this.jobService.update(id, jobRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable("id") long id) throws PermissionException {
        boolean isDeleted = jobService.delete(id);
        if (isDeleted) {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(SkillConstants.MESSAGE_200)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.EXPECTATION_FAILED.value())
                    .message(SkillConstants.MESSAGE_417_DELETE)
                    .build();
        }
    }
}
