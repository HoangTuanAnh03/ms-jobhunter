package com.tuananh.authservice.controller;

import com.tuananh.authservice.advice.exception.IdInvalidException;
import com.tuananh.authservice.dto.request.CreateUserRequest;
import com.tuananh.authservice.dto.request.UpdateUserRequest;
import com.tuananh.authservice.dto.response.ResUserDTO;
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

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @ApiMessage("fetch user by id")
    @GetMapping("/{id}")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.fetchResUserDtoById(id));
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
    public ResponseEntity<ResUserDTO> updateUser(@PathVariable("id") String id, @Valid @RequestBody UpdateUserRequest updateUserRequest) throws IdInvalidException {
        return ResponseEntity.ok(this.userService.handleUpdateUser(id, updateUserRequest));
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
