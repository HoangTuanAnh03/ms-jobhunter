package com.tuananh.jobservice.controller;


import com.tuananh.jobservice.dto.ApiResponse;
import com.tuananh.jobservice.dto.request.CreateSkillRequest;
import com.tuananh.jobservice.dto.request.UpdateSkillRequest;
import com.tuananh.jobservice.dto.response.ResultPaginationDTO;
import com.tuananh.jobservice.dto.response.SkillResponse;
import com.tuananh.jobservice.entity.Skill;
import com.tuananh.jobservice.service.SkillService;
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


/**
 * @author Tuan Anh Hoang
 */

@Tag(
        name = "CRUD REST APIs for Skill in JobHunter",
        description = "CRUD REST APIs in JobHunter to CREATE, UPDATE, FETCH AND DELETE skill details"
)
@RestController
@RequestMapping("/skills")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkillController {
    SkillService skillService;

    @GetMapping("/fetchById/{id}")
    public ApiResponse<Skill> getById(@PathVariable("id") long id) {
        Skill skill = this.skillService.fetchById(id);
        return ApiResponse.<Skill>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch skill by id")
                .data(this.skillService.fetchById(id))
                .build();
    }

    @GetMapping("/pagination")
    public ApiResponse<ResultPaginationDTO> getPermissions(
            @Filter Specification<Skill> spec, Pageable pageable) {

        return ApiResponse.<ResultPaginationDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch skills")
                .data(this.skillService.fetchPagination(spec, pageable))
                .build();
    }


    @PostMapping("/create")
    public ApiResponse<SkillResponse> create(@Valid @RequestBody CreateSkillRequest skillRequest) {

        return ApiResponse.<SkillResponse>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create a skill")
                .data(this.skillService.create(skillRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<SkillResponse> update(@PathVariable("id") long id, @Valid @RequestBody UpdateSkillRequest skillRequest) {
        return ApiResponse.<SkillResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Update a skill")
                .data(this.skillService.update(id, skillRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable("id") long id) {
        boolean isDeleted = skillService.delete(id);
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
