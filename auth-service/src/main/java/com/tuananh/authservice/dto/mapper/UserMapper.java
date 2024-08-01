package com.tuananh.authservice.dto.mapper;

import com.tuananh.authservice.dto.response.SimpInfoUserResponse;
import com.tuananh.authservice.dto.response.UserResponse;
import com.tuananh.authservice.entity.User;
import com.tuananh.authservice.service.client.CompanyClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMapper {
    CompanyClient companyClient;

    public UserResponse toUserResponse(User user) {
        UserResponse.CompanyUser companyUser = null;

        if (user.getCompanyId() != 0) {
            var company = companyClient.getById(5).getData();
            companyUser = UserResponse.CompanyUser.builder()
                    .id(company.getId())
                    .name(company.getName())
                    .build();
        }

        var roleUser = UserResponse.RoleUser.builder()
                .id(user.getRole().getId())
                .name(user.getRole().getName())
                .build();

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

    public SimpInfoUserResponse toSimpInfoUserResponse(User user) {
        return SimpInfoUserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .dob(user.getDob())
                .gender(user.getGender())
                .address(user.getAddress())
                .email(user.getEmail())
                .mobileNumber(user.getMobileNumber())
                .build();
    }
}
