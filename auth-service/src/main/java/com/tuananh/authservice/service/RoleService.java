package com.tuananh.authservice.service;

import com.tuananh.authservice.advice.exception.IdInvalidException;
import com.tuananh.authservice.dto.request.CreateRoleRequest;
import com.tuananh.authservice.dto.request.UpdateRoleRequest;
import com.tuananh.authservice.dto.response.ResultPaginationDTO;
import com.tuananh.authservice.entity.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface RoleService {
    /**
     * @param name - Role name
     * @return boolean indicating if the name already exited or not
     */
    boolean existByName(String name);

    /**
     * @param roleId - Role id
     * @return Role Object on a given roleId
     */
    Role fetchById(long roleId);

    /**
     * @param spec - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable);

    /**
     * @param createRoleRequest - CreateRoleRequest Object
     * @return Role Object saved to database
     */
    Role create(CreateRoleRequest createRoleRequest);

    /**
     * @param updateRoleRequest -UpdateRoleRequest Object
     * @return Role Object updated to database
     */
    Role update(int id, UpdateRoleRequest updateRoleRequest) throws IdInvalidException;

    /**
     * @param id - Input UserId
     * @return boolean indicating if the delete of Role details is successful or not
     */
    boolean delete(long id);
}
