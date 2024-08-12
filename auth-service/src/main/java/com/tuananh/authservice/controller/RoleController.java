package com.tuananh.authservice.controller;

import com.tuananh.authservice.advice.ErrorCode;
import com.tuananh.authservice.dto.request.CreateRoleRequest;
import com.tuananh.authservice.dto.request.UpdateRoleRequest;
import com.tuananh.authservice.dto.response.ResultPaginationDTO;
import com.tuananh.authservice.entity.Role;
import com.tuananh.authservice.service.RoleService;
import com.tuananh.authservice.advice.exception.IdInvalidException;
import com.tuananh.authservice.util.constant.UsersConstants;
import com.turkraft.springfilter.boot.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
        name = "CRUD REST APIs for Roles in JobHunter",
        description = "CRUD REST APIs in JobHunter to CREATE, UPDATE, FETCH AND DELETE role details"
)
@RestController
@RequestMapping("/roles")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @Operation(
            summary = "Fetch Role Details REST API",
            description = "REST API to fetch Role details based on a roleId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    }
    )
    @GetMapping("/{id}")
    public com.tuananh.authservice.dto.ApiResponse<Role> getById(@PathVariable("id") long id) throws IdInvalidException {

        Role role = this.roleService.fetchById(id);
        if (role == null) {
            throw new IdInvalidException("RoleId = " + id + " not exited");
        }

        return com.tuananh.authservice.dto.ApiResponse.<Role>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch role by id")
                .data(role)
                .build();
    }

    @GetMapping("")
    public com.tuananh.authservice.dto.ApiResponse<ResultPaginationDTO> getPermissions(
            @Filter Specification<Role> spec, Pageable pageable) {

        return com.tuananh.authservice.dto.ApiResponse.<ResultPaginationDTO>builder()
                .code(HttpStatus.OK.value())
                .message("Fetch roles")
                .data(this.roleService.getRoles(spec, pageable))
                .build();
    }

    @Operation(
            summary = "Create Role REST API",
            description = "REST API to create new Role inside JobHunter"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    }
    )
    @PostMapping("")
    public com.tuananh.authservice.dto.ApiResponse<Role> create(@Valid @RequestBody CreateRoleRequest roleRequest) {

        return com.tuananh.authservice.dto.ApiResponse.<Role>builder()
                .code(HttpStatus.CREATED.value())
                .message("Create a role")
                .data(this.roleService.create(roleRequest))
                .build();
    }

    @Operation(
            summary = "Update Role Details REST API",
            description = "REST API to update Role details based on a roleId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorCode.class)
                    )
            )
    }
    )
    @PutMapping("/{id}")
    public com.tuananh.authservice.dto.ApiResponse<Role> update(@PathVariable("id") int id, @Valid @RequestBody UpdateRoleRequest updateRoleRequest) {

        return com.tuananh.authservice.dto.ApiResponse.<Role>builder()
                .code(HttpStatus.CREATED.value())
                .message("Update a role")
                .data(this.roleService.update(id, updateRoleRequest))
                .build();
    }

    @Operation(
            summary = "Delete Role & Customer Details REST API",
            description = "REST API to delete Role details based on a roleId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            )
    }
    )
    @DeleteMapping("/{id}")
    public com.tuananh.authservice.dto.ApiResponse<?> delete(@PathVariable("id") long id) {
        boolean isDeleted = roleService.delete(id);
        if (isDeleted) {
            return com.tuananh.authservice.dto.ApiResponse.<String>builder()
                    .code(HttpStatus.CREATED.value())
                    .message(UsersConstants.MESSAGE_200)
                    .data(null)
                    .build();
        } else {
            return com.tuananh.authservice.dto.ApiResponse.<String>builder()
                    .code(HttpStatus.EXPECTATION_FAILED.value())
                    .message(UsersConstants.MESSAGE_417_DELETE)
                    .data(null)
                    .build();
        }
    }
}
