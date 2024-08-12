package com.tuananh.companyservice.controller;


import com.tuananh.companyservice.dto.ApiResponse;
import com.tuananh.companyservice.dto.request.ChangeStatusCompanyRequest;
import com.tuananh.companyservice.dto.request.CreateCompanyRequest;
import com.tuananh.companyservice.dto.request.UpdateCompanyRequest;
import com.tuananh.companyservice.dto.response.IntegrateInfoCompanyRes;
import com.tuananh.companyservice.dto.response.ResultPaginationDTO;
import com.tuananh.companyservice.entity.Company;
import com.tuananh.companyservice.service.CompanyService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;


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

    @GetMapping("/fetchById/{id}")
    public ApiResponse<IntegrateInfoCompanyRes> fetchById(@PathVariable("id") long id) {
        return ApiResponse.<IntegrateInfoCompanyRes>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch company by id")
                .data(this.companyService.fetchById(id))
                .build();
    }

    @GetMapping("/fetchByIdIn")
    public ApiResponse<List<Company>> fetchByIdIn(@RequestParam List<Long> ids) {
        return ApiResponse.<List<Company>>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch companies by ids")
                .data(this.companyService.fetchByIdIn(ids))
                .build();
    }

    @GetMapping("/pagination")
    public ApiResponse<ResultPaginationDTO> getCompanies(
            @Filter Specification<Company> spec, Pageable pageable) {

        return ApiResponse.<ResultPaginationDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch companies")
                .data(this.companyService.getCompanies(spec, pageable))
                .build();
    }

    @PostMapping("")
    public ApiResponse<Company> create(@Valid @RequestBody CreateCompanyRequest companyRequest) {
        return ApiResponse.<Company>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create a company")
                .data(this.companyService.create(companyRequest))
                .build();
    }

    @PutMapping("/changeStatus/{id}")
    public ApiResponse<Company> changeStatus(@PathVariable long id, @Valid @RequestBody ChangeStatusCompanyRequest changeStatusCompanyRequest) {
        return ApiResponse.<Company>builder()
                .code(HttpStatus.OK.value())
                .message("Change status a company")
                .data(this.companyService.changeStatus(id, changeStatusCompanyRequest))
                .build();
    }

    @PutMapping("")
    public ApiResponse<Company> update(@Valid @RequestBody UpdateCompanyRequest updateCompanyRequest) {
        return ApiResponse.<Company>builder()
                .code(HttpStatus.OK.value())
                .message("Update a company")
                .data(this.companyService.update(updateCompanyRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> delete(@PathVariable("id") long id) {
        boolean isDeleted = companyService.delete(id);
        if (isDeleted) {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(CompanyConstants.MESSAGE_200)
                    .build();
        } else {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.EXPECTATION_FAILED.value())
                    .message(CompanyConstants.MESSAGE_417_DELETE)
                    .build();
        }
    }
}
