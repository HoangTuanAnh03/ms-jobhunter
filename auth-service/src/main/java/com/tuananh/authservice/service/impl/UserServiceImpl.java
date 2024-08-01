package com.tuananh.authservice.service.impl;

import com.tuananh.authservice.advice.AppException;
import com.tuananh.authservice.advice.ErrorCode;
import com.tuananh.authservice.advice.exception.PermissionException;
import com.tuananh.authservice.advice.exception.ResourceNotFoundException;
import com.tuananh.authservice.dto.mapper.UserMapper;
import com.tuananh.authservice.dto.request.CreateUserRequest;
import com.tuananh.authservice.dto.request.PasswordCreationRequest;
import com.tuananh.authservice.dto.request.UpdateUserRequest;
import com.tuananh.authservice.dto.response.ResultPaginationDTO;
import com.tuananh.authservice.dto.response.SimpInfoUserResponse;
import com.tuananh.authservice.dto.response.UserResponse;
import com.tuananh.authservice.entity.Role;
import com.tuananh.authservice.entity.User;
import com.tuananh.authservice.repository.RoleRepository;
import com.tuananh.authservice.repository.UserRepository;
import com.tuananh.authservice.service.UserService;
import com.tuananh.authservice.util.SecurityUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    SecurityUtil securityUtil;

    /**
     * @param email - Input email
     * @return boolean indicating if the email already exited or not
     */
    @Override
    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    /**
     * @param id - Input UserId
     * @return User Object on a given userId
     */
    @Override
    public User fetchUserById(String id) {
        return this.userRepository.findById(id).orElse(null);
    }

    /**
     * @return UserResponse Object - Info currentUser
     */
    @Override
    public UserResponse fetchMyInfo() {
        String email = securityUtil.getCurrentUserLogin().orElse(null);
        return this.userMapper.toUserResponse(Objects.requireNonNull(userRepository.findByEmail(email).orElse(null)));
    }

    /**
     * @param companyId - Input companyId
     * @return UserResponse Object
     */
    @Override
    public UserResponse handleUpdateHR(long companyId) throws PermissionException {
        String email = securityUtil.getCurrentUserLogin().orElse(null);

        User user = userRepository.findByEmail(email).orElse(null);

        Role roleHR = roleRepository.findByName("HR").orElseThrow(
                () -> new ResourceNotFoundException("Role", "roleName", "HR")
        );

        if (userRepository.existsByCompanyIdAndRole(companyId, roleHR)){
            throw new PermissionException("Forbidden");
        }

        if (user != null) {
            user.setCompanyId(companyId);
            user.setRole(roleHR);
            userRepository.save(user);
        }

        return userMapper.toUserResponse(user);
    }

    /**
     * @param request - password
     */
    @Override
    public void createPassword(PasswordCreationRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (StringUtils.hasText(user.getPassword()))
            throw new AppException(ErrorCode.PASSWORD_EXISTED);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    /**
     * @param email - Input email
     * @return User Object on a given email
     */
    @Override
    public User fetchUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }

    /**
     * @param id - Input UserId
     * @return User Details based on a given data updated to database
     */
    @Override
    public UserResponse fetchResUserDtoById(String id) {
        User user = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", id)
        );

        return this.userMapper.toUserResponse(user);
    }

    /**
     * @param spec - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<UserResponse> listUser = pageUser.getContent()
                .stream().map(this.userMapper::toUserResponse)
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    /**
     * @param newUser - Input CreateUserRequest Object
     * @return User Details based on a given data saved to database
     */
    @Override
    public UserResponse handleCreateUser(CreateUserRequest newUser) {
        // check company
//        if (user.getCompany() != null) {
//            Optional<Company> companyOptional = this.companyService.findById(user.getCompany().getId());
//            user.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
//        }

        Role role = roleRepository.findByName("USER").orElseThrow(
                () -> new ResourceNotFoundException("Role", "roleName", "USER")
        );

        User user = User.builder()
                .id("")
                .name(newUser.getName())
                .gender(newUser.getGender())
                .address(newUser.getAddress())
                .dob(newUser.getDob())
                .email(newUser.getEmail())
                .role(role)
                .password(passwordEncoder.encode(newUser.getPassword()))
                .build();

        userRepository.save(user);

        return this.userMapper.toUserResponse(user);
    }

    /**
     * @param id - Input UserId
     * @param updateUserRequest - Input UpdateUserRequest Object
     * @return User Details based on a given data updated to database
     */
    @Override
    public UserResponse handleUpdateUser(String id, UpdateUserRequest updateUserRequest) {
        User currentUser = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", id)
        );

        currentUser.setName(updateUserRequest.getName());
        currentUser.setAddress(updateUserRequest.getAddress());
        currentUser.setGender(updateUserRequest.getGender());
        currentUser.setDob(updateUserRequest.getDob());
        currentUser.setMobileNumber(updateUserRequest.getMobileNumber());
        //  set company

        // check company
//            if (reqUser.getCompany() != null) {
//                Optional<Company> companyOptional = this.companyService.findById(reqUser.getCompany().getId());
//                currentUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
//            }

        currentUser = this.userRepository.save(currentUser);
        return this.userMapper.toUserResponse(currentUser);
    }

    /**
     * @param id - Input UserId
     * @return boolean indicating if the delete of User details is successful or not
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public boolean handleDeleteUser(String id) {
        User user = this.userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "userId", id)
        );
        this.userRepository.deleteById(user.getId());
        return true;
    }

    /**
     * @param ids
     * @return
     */
    @Override
    public List<SimpInfoUserResponse> fetchUserByIdIn(List<String> ids) {
        return userRepository.findByIdIn(ids).stream().map(userMapper::toSimpInfoUserResponse).toList();
    }
}
