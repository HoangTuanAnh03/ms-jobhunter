package com.tuananh.companyservice.controller;


import com.tuananh.companyservice.dto.request.ChangeStatusCompanyRequest;
import com.tuananh.companyservice.dto.request.CreateCompanyRequest;
import com.tuananh.companyservice.dto.request.UpdateCompanyRequest;
import com.tuananh.companyservice.dto.response.IntegrateInfoCompanyRes;
import com.tuananh.companyservice.dto.response.ResultPaginationDTO;
import com.tuananh.companyservice.entity.Company;
import com.tuananh.companyservice.service.CompanyService;
import com.tuananh.companyservice.util.annotation.ApiMessage;
import com.tuananh.companyservice.util.constant.CompanyConstants;
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
        name = "CRUD REST APIs for Company in JobHunter",
        description = "CRUD REST APIs in JobHunter to CREATE, UPDATE, FETCH AND DELETE company details"
)
@RestController
@RequestMapping("/company")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {
    CompanyService companyService;

    @GetMapping("/{id}")
    @ApiMessage("Fetch company by id")
    public ResponseEntity<IntegrateInfoCompanyRes> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(this.companyService.fetchById(id));
    }

    @GetMapping("/pagination")
    @ApiMessage("Fetch companies")
    public ResponseEntity<ResultPaginationDTO> getCompanies(
            @Filter Specification<Company> spec, Pageable pageable) {

        return ResponseEntity.ok(this.companyService.getCompanies(spec, pageable));
    }

    @PostMapping("")
    @ApiMessage("Create a company")
    public ResponseEntity<Company> create(@Valid @RequestBody CreateCompanyRequest companyRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.companyService.create(companyRequest));
    }

    @PutMapping("/changeStatus/{id}")
    @ApiMessage("Change status a company")
    public ResponseEntity<Company> changeStatus(@PathVariable long id, @Valid @RequestBody ChangeStatusCompanyRequest changeStatusCompanyRequest) {
        return ResponseEntity.ok().body(this.companyService.changeStatus(id, changeStatusCompanyRequest));
    }

    @PutMapping("")
    @ApiMessage("Update a company")
    public ResponseEntity<Company> update(@Valid @RequestBody UpdateCompanyRequest updateCompanyRequest) {
        return ResponseEntity.ok().body(this.companyService.update(updateCompanyRequest));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a company")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        boolean isDeleted = companyService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok(CompanyConstants.MESSAGE_200);
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(CompanyConstants.MESSAGE_417_DELETE);
        }
    }
}
