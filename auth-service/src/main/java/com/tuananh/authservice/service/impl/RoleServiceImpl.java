package com.tuananh.authservice.service.impl;

import com.tuananh.authservice.advice.exception.DuplicateRecordException;
import com.tuananh.authservice.advice.exception.IdInvalidException;
import com.tuananh.authservice.advice.exception.ResourceNotFoundException;
import com.tuananh.authservice.dto.request.CreateRoleRequest;
import com.tuananh.authservice.dto.request.UpdateRoleRequest;
import com.tuananh.authservice.dto.response.ResultPaginationDTO;
import com.tuananh.authservice.entity.Role;
import com.tuananh.authservice.repository.RoleRepository;
import com.tuananh.authservice.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {
    RoleRepository roleRepository;
    /**
     * @param name - Role name
     * @return boolean indicating if the name already exited or not
     */
    @Override
    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    /**
     * @param roleId - Role id
     * @return Role Object on a given roleId
     */
    @Override
    public Role fetchById(long roleId) {
        return this.roleRepository.findById(roleId).orElse(null);
    }

    /**
     * @param spec - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    @Override
    public ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pRole = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pRole.getTotalPages());
        mt.setTotal(pRole.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pRole.getContent());
        return rs;
    }

    /**
     * @param createRoleRequest - CreateRoleRequest Object
     * @return Role Object saved to database
     */
    @Override
    public Role create(CreateRoleRequest createRoleRequest) {
        if (existByName(createRoleRequest.getName())) {
            throw new DuplicateRecordException("ROLE", "Name", createRoleRequest.getName());
        }

        Role newRole = Role.builder()
                .name(createRoleRequest.getName())
                .description(createRoleRequest.getDescription())
                .active(true).build();

        return this.roleRepository.save(newRole);
    }

    /**
     * @param updateRoleRequest -UpdateRoleRequest Object
     * @return Role Object updated to database
     */
    @Override
    public Role update(int id, UpdateRoleRequest updateRoleRequest) throws IdInvalidException {
        Role currentRole = this.fetchById(id);

        Role newRoleByName = roleRepository.findByName(updateRoleRequest.getName()).orElse(null);

        if (newRoleByName != null && newRoleByName.getId() != currentRole.getId()
                && currentRole.getName().equals(newRoleByName.getName())) {
            throw new DuplicateRecordException("ROLE", "Name", newRoleByName.getName());
        }

        currentRole.setName(updateRoleRequest.getName());
        currentRole.setDescription(updateRoleRequest.getDescription());
        currentRole.setActive(updateRoleRequest.isActive());

        currentRole = this.roleRepository.save(currentRole);
        return currentRole;
    }

    /**
     * @param id - Input UserId
     * @return boolean indicating if the delete of Role details is successful or not
     */
    @Override
    public boolean delete(long id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Role", "roleId", id)
        );
        this.roleRepository.deleteById(role.getId());
        return true;
    }


}
