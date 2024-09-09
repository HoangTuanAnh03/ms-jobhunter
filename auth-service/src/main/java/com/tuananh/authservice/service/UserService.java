package com.tuananh.authservice.service;

import com.tuananh.authservice.advice.exception.PermissionException;
import com.tuananh.authservice.dto.request.CreateUserRequest;
import com.tuananh.authservice.dto.request.PasswordCreationRequest;
import com.tuananh.authservice.dto.request.UpdateUserRequest;
import com.tuananh.authservice.dto.response.SimpInfoUserResponse;
import com.tuananh.authservice.dto.response.UserResponse;
import com.tuananh.authservice.dto.response.ResultPaginationDTO;
import com.tuananh.authservice.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {
    /**
     * @param email - Input email
     * @return boolean indicating if the email already exited or not
     */
    boolean isEmailExistAndActive(String email);

    /**
     * @param id - Input UserId
     * @return User Object on a given userId
     */
    User fetchUserById(String id);

    /**
     * @return UserResponse Object - Info currentUser
     */
    UserResponse fetchMyInfo();

    /**
     * @param companyId - Input companyId
     * @return UserResponse Object
     */
    UserResponse handleUpdateHR(long companyId) throws PermissionException;

    /**
     * @param request - password
     */
    void createPassword(PasswordCreationRequest request);

    /**
     * @param email - Input email
     * @return User Object on a given email
     */
    User fetchUserByEmail(String email);

    /**
     * @param id - Input UserId
     * @return User Details based on a given data updated to database
     */
    UserResponse fetchResUserDtoById(String id) ;

    /**
     * @param spec - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable);

    /**
     * @param newUser - Input CreateUserRequest Object
     * @return User Details based on a given data saved to database
     */
    UserResponse handleCreateUser(CreateUserRequest newUser);

    /**
     * @param id - Input UserId
     * @param updateUserRequest - Input UpdateUserRequest Object
     * @return User Details based on a given data updated to database
     */
    UserResponse handleUpdateUser(String id, UpdateUserRequest updateUserRequest);

    /**
     * @param id - Input UserId
     * @return boolean indicating if the delete of User details is successful or not
     */
    boolean handleDeleteUser(String id);

    List<SimpInfoUserResponse> fetchUserByIdIn(List<String> ids);

    /**
     * @return UserResponse Object - Info currentUser
     */

}
