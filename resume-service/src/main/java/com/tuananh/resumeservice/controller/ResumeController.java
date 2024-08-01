package com.tuananh.resumeservice.controller;


import com.tuananh.resumeservice.advice.exception.PermissionException;
import com.tuananh.resumeservice.dto.request.CreateResumeRequest;
import com.tuananh.resumeservice.dto.request.UpdateResumeRequest;
import com.tuananh.resumeservice.dto.response.ResultPaginationDTO;
import com.tuananh.resumeservice.dto.response.ResumeResponse;
import com.tuananh.resumeservice.entity.Resume;
import com.tuananh.resumeservice.service.ResumeService;
import com.tuananh.resumeservice.util.CustomHeaders;
import com.tuananh.resumeservice.util.annotation.ApiMessage;
import com.tuananh.resumeservice.util.constant.ResumeConstants;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

//    @GetMapping("/fetchById/{id}")
//    @ApiMessage("Fetch resume by id")
//    public ResponseEntity<Resume> getById(@PathVariable("id") long id) {
//        return ResponseEntity.ok().body(resumeService.fetchById(id));
//    }

    @GetMapping("/myResume/pagination")
    @ApiMessage("Fetch my resume")
    public ResponseEntity<ResultPaginationDTO> fetchMyResume(@RequestHeader(CustomHeaders.X_AUTH_USER_ID) String id, Pageable pageable) {

        return ResponseEntity.ok(this.resumeService.fetchMyResume(id, pageable));
    }

    @GetMapping("/resumeByJob/pagination")
    @ApiMessage("Fetch resume by jobId")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByJob(@RequestParam long jobId, Pageable pageable) throws PermissionException {

        return ResponseEntity.ok(this.resumeService.fetchResumeByJob(jobId, pageable));
    }

    @PostMapping("/create")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResumeResponse> create(@Valid @RequestBody CreateResumeRequest resumeRequest) throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.resumeService.create(resumeRequest));
    }

    @PutMapping("/{id}")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResumeResponse> update(@PathVariable("id") long id, @Valid @RequestBody UpdateResumeRequest resumeRequest) {
        return ResponseEntity.ok().body(this.resumeService.update(id, resumeRequest));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a resume")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        boolean isDeleted = resumeService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok()
                    .body(ResumeConstants.MESSAGE_200);
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(ResumeConstants.MESSAGE_417_DELETE);
        }
    }
}
