package com.tuananh.authservice.controller;

import com.tuananh.authservice.advice.ErrorCode;
import com.tuananh.authservice.dto.RestResponse;
import com.tuananh.authservice.dto.request.CreateRoleRequest;
import com.tuananh.authservice.dto.request.UpdateRoleRequest;
import com.tuananh.authservice.dto.response.ResultPaginationDTO;
import com.tuananh.authservice.entity.Role;
import com.tuananh.authservice.service.RoleService;
import com.tuananh.authservice.util.annotation.ApiMessage;
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
import org.springframework.http.ResponseEntity;
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
                            schema = @Schema(implementation = RestResponse.class)
                    )
            )
    }
    )
    @GetMapping("/{id}")
    @ApiMessage("Fetch role by id")
    public ResponseEntity<Role> getById(@PathVariable("id") long id) throws IdInvalidException {

        Role role = this.roleService.fetchById(id);
        if (role == null) {
            throw new IdInvalidException("RoleId = " + id + " not exited");
        }

        return ResponseEntity.ok().body(role);
    }

    @GetMapping("")
    @ApiMessage("Fetch roles")
    public ResponseEntity<ResultPaginationDTO> getPermissions(
            @Filter Specification<Role> spec, Pageable pageable) {

        return ResponseEntity.ok(this.roleService.getRoles(spec, pageable));
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
                            schema = @Schema(implementation = RestResponse.class)
                    )
            )
    }
    )
    @PostMapping("")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> create(@Valid @RequestBody CreateRoleRequest roleRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.roleService.create(roleRequest));
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
    @ApiMessage("Update a role")
    public ResponseEntity<Role> update(@PathVariable("id") int id, @Valid @RequestBody UpdateRoleRequest updateRoleRequest) {
        return ResponseEntity.ok().body(this.roleService.update(id, updateRoleRequest));
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
                            schema = @Schema(implementation = RestResponse.class)
                    )
            )
    }
    )
    @DeleteMapping("/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        boolean isDeleted = roleService.delete(id);
        if (isDeleted) {
            return ResponseEntity.ok(UsersConstants.MESSAGE_200);
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(UsersConstants.MESSAGE_417_DELETE);
        }
    }
}
