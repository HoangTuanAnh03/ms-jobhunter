package com.tuananh.resumeservice.controller;


import com.tuananh.resumeservice.advice.exception.PermissionException;
import com.tuananh.resumeservice.dto.ApiResponse;
import com.tuananh.resumeservice.dto.request.CreateResumeRequest;
import com.tuananh.resumeservice.dto.request.UpdateResumeRequest;
import com.tuananh.resumeservice.dto.response.ResultPaginationDTO;
import com.tuananh.resumeservice.dto.response.ResumeResponse;
import com.tuananh.resumeservice.service.ResumeService;
import com.tuananh.resumeservice.util.CustomHeaders;
import com.tuananh.resumeservice.util.constant.ResumeConstants;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


/**
 * @author Tuan Anh Hoang
 */

@Tag(
        name = "CRUD REST APIs for Jobs in JobHunter",
        description = "CRUD REST APIs in JobHunter to CREATE, UPDATE, FETCH AND DELETE job details"
)
@RestController
@RequestMapping("/resume")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ResumeController {
    ResumeService resumeService;


    @GetMapping("/myResume/pagination")
    public ApiResponse<ResultPaginationDTO> fetchMyResume(@RequestHeader(CustomHeaders.X_AUTH_USER_ID) String id, Pageable pageable) {

        return ApiResponse.<ResultPaginationDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch my resume")
                .data(this.resumeService.fetchMyResume(id, pageable))
                .build();
    }

    @GetMapping("/resumeByJob/pagination")
    public ApiResponse<ResultPaginationDTO> fetchResumeByJob(@RequestParam long jobId, Pageable pageable) throws PermissionException {

        return ApiResponse.<ResultPaginationDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch resume by jobId")
                .data(this.resumeService.fetchResumeByJob(jobId, pageable))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<ResumeResponse> create(@Valid @RequestBody CreateResumeRequest resumeRequest) throws Exception {

        return ApiResponse.<ResumeResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Create a resume")
                .data(this.resumeService.create(resumeRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ResumeResponse> update(@PathVariable("id") long id, @Valid @RequestBody UpdateResumeRequest resumeRequest) {
        return ApiResponse.<ResumeResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update a resume")
                .data(this.resumeService.update(id, resumeRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable("id") long id) {
        boolean isDeleted = resumeService.delete(id);
        if (isDeleted) {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(ResumeConstants.MESSAGE_200)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.EXPECTATION_FAILED.value())
                    .message(ResumeConstants.MESSAGE_417_DELETE)
                    .build();
        }
    }
}
