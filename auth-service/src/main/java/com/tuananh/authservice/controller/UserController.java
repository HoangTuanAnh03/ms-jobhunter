package com.tuananh.authservice.controller;

import com.tuananh.authservice.advice.exception.IdInvalidException;
import com.tuananh.authservice.advice.exception.PermissionException;
import com.tuananh.authservice.dto.request.PasswordCreationRequest;
import com.tuananh.authservice.dto.request.UpdateUserRequest;
import com.tuananh.authservice.dto.response.SimpInfoUserResponse;
import com.tuananh.authservice.dto.response.UserResponse;
import com.tuananh.authservice.dto.response.ResultPaginationDTO;
import com.tuananh.authservice.entity.User;
import com.tuananh.authservice.service.UserService;
import com.tuananh.authservice.util.annotation.ApiMessage;
import com.tuananh.authservice.util.constant.UsersConstants;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping("/create-password")
    public ResponseEntity<?> createPassword(@RequestBody @Valid PasswordCreationRequest request) {
        userService.createPassword(request);
        return ResponseEntity.ok()
                            .body("Password has been created, you could use it to log-in");
    }

    @ApiMessage("fetch user by id")
    @GetMapping("/fetchUserById/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.fetchResUserDtoById(id));
    }

    @ApiMessage("fetch users by id")
    @GetMapping("/fetchUserByIdIn")
    public ResponseEntity<List<SimpInfoUserResponse>> fetchUserByIdIn(@RequestParam("ids") List<String> ids) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.fetchUserByIdIn(ids));
    }

    @ApiMessage("fetch my info")
    @GetMapping("/my-info")
    public ResponseEntity<UserResponse> getMyInfo() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.fetchMyInfo());
    }

    @ApiMessage("fetch all users")
    @GetMapping("")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.fetchAllUser(spec, pageable));
    }


    @ApiMessage("Update a user")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") String id, @Valid @RequestBody UpdateUserRequest updateUserRequest) throws IdInvalidException {
        return ResponseEntity.ok(this.userService.handleUpdateUser(id, updateUserRequest));
    }

    @ApiMessage("Update role HR")
    @PutMapping("/updateHR/{companyId}")
    public ResponseEntity<UserResponse> updateHR(@PathVariable(name = "companyId") long companyId) throws PermissionException {
        return ResponseEntity.ok(this.userService.handleUpdateHR(companyId));
    }

    @ApiMessage("Delete a user")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
        boolean isDeleted = userService.handleDeleteUser(id);
        if (isDeleted) {
            return ResponseEntity.ok(UsersConstants.MESSAGE_200);
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(UsersConstants.MESSAGE_417_DELETE);
        }
    }
}
