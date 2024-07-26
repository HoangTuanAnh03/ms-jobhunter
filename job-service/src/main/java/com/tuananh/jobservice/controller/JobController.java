package com.tuananh.jobservice.controller;


import com.tuananh.jobservice.advice.exception.PermissionException;
import com.tuananh.jobservice.dto.request.CreateJobRequest;
import com.tuananh.jobservice.dto.request.UpdateJobRequest;
import com.tuananh.jobservice.dto.response.ResultPaginationDTO;
import com.tuananh.jobservice.entity.Job;
import com.tuananh.jobservice.service.JobService;
import com.tuananh.jobservice.util.annotation.ApiMessage;
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
@RequestMapping("/jobs")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JobController {
    JobService jobService;

    @GetMapping("/fetchById/{id}")
    @ApiMessage("Fetch skill by id")
    public ResponseEntity<Job> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(jobService.fetchById(id));
    }

    @GetMapping("/pagination")
    @ApiMessage("Fetch skills")
    public ResponseEntity<ResultPaginationDTO> fetchPagination(
            @Filter Specification<Job> spec, Pageable pageable) {

        return ResponseEntity.ok(this.jobService.fetchPagination(spec, pageable));
    }

    @PostMapping("/create")
    @ApiMessage("Create a skill")
    public ResponseEntity<Job> create(@Valid @RequestBody CreateJobRequest jobRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.jobService.create(jobRequest));
    }

    @PutMapping("/{id}")
    @ApiMessage("Update a skill")
    public ResponseEntity<Job> update(@PathVariable("id") long id, @Valid @RequestBody UpdateJobRequest jobRequest)
            throws PermissionException {
        return ResponseEntity.ok().body(this.jobService.update(id, jobRequest));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<?> delete(@PathVariable("id") long id) throws PermissionException {
        boolean isDeleted = jobService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok()
                    .body(SkillConstants.MESSAGE_200);
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(SkillConstants.MESSAGE_417_DELETE);
        }
    }
}
