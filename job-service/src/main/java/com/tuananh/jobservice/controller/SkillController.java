package com.tuananh.jobservice.controller;


import com.tuananh.jobservice.dto.request.CreateSkillRequest;
import com.tuananh.jobservice.dto.request.UpdateSkillRequest;
import com.tuananh.jobservice.dto.response.ResultPaginationDTO;
import com.tuananh.jobservice.dto.response.SkillResponse;
import com.tuananh.jobservice.entity.Skill;
import com.tuananh.jobservice.service.SkillService;
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
    @ApiMessage("Fetch skill by id")
    public ResponseEntity<Skill> getById(@PathVariable("id") long id) {
        Skill skill = this.skillService.fetchById(id);
        return ResponseEntity.ok().body(skill);
    }

    @GetMapping("/pagination")
    @ApiMessage("Fetch skills")
    public ResponseEntity<ResultPaginationDTO> getPermissions(
            @Filter Specification<Skill> spec, Pageable pageable) {

        return ResponseEntity.ok(this.skillService.fetchPagination(spec, pageable));
    }


    @PostMapping("/create")
    @ApiMessage("Create a skill")
    public ResponseEntity<SkillResponse> create(@Valid @RequestBody CreateSkillRequest skillRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.skillService.create(skillRequest));
    }

    @PutMapping("/{id}")
    @ApiMessage("Update a skill")
    public ResponseEntity<SkillResponse> update(@PathVariable("id") long id, @Valid @RequestBody UpdateSkillRequest skillRequest) {
        return ResponseEntity.ok().body(this.skillService.update(id, skillRequest));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        boolean isDeleted = skillService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok(SkillConstants.MESSAGE_200);
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(SkillConstants.MESSAGE_417_DELETE);
        }
    }
}
