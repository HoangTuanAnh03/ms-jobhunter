package com.tuananh.authservice.service;

import com.tuananh.authservice.dto.request.CreateUserRequest;
import com.tuananh.authservice.dto.request.UpdateUserRequest;
import com.tuananh.authservice.dto.response.ResUserDTO;
import com.tuananh.authservice.dto.response.ResultPaginationDTO;
import com.tuananh.authservice.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;

public interface UserService {
    /**
     * @param email - Input email
     * @return boolean indicating if the email already exited or not
     */
    boolean isEmailExist(String email);

    /**
     * @param id - Input UserId
     * @return User Object on a given userId
     */
    User fetchUserById(String id);

    /**
     * @param email - Input email
     * @return User Object on a given email
     */
    User fetchUserByEmail(String email);

    /**
     * @param id - Input UserId
     * @return User Details based on a given data updated to database
     */
    ResUserDTO fetchResUserDtoById(String id) ;

    /**
     * @param spec - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    @PreAuthorize("hasRole('ADMIN')")
    ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable);

    /**
     * @param newUser - Input CreateUserRequest Object
     * @return User Details based on a given data saved to database
     */
    ResUserDTO handleCreateUser(CreateUserRequest newUser);

    /**
     * @param id - Input UserId
     * @param updateUserRequest - Input UpdateUserRequest Object
     * @return User Details based on a given data updated to database
     */
    ResUserDTO handleUpdateUser(String id, UpdateUserRequest updateUserRequest);

    /**
     * @param id - Input UserId
     * @return boolean indicating if the delete of User details is successful or not
     */
    @PreAuthorize("hasRole('ADMIN')")
    boolean handleDeleteUser(String id);
}
