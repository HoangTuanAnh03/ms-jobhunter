package com.tuananh.authservice.dto.mapper;

import com.tuananh.authservice.dto.response.UserResponse;
import com.tuananh.authservice.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@AllArgsConstructor
public class UserMapper {
    public UserResponse toUserResponse(User user) {

        UserResponse.CompanyUser companyUser = UserResponse.CompanyUser.builder().id(user.getCompanyId()).name("null").build();
        UserResponse.RoleUser roleUser = UserResponse.RoleUser.builder().id(user.getRole().getId()).name(user.getRole().getName()).build();

        return UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .dob(user.getDob())
                        .gender(user.getGender())
                        .address(user.getAddress())
                        .email(user.getEmail())
                        .company(companyUser)
                        .role(roleUser).noPassword(!StringUtils.hasText(user.getPassword()))
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .build();
    }
}
