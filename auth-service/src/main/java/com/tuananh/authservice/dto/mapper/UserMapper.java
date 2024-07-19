package com.tuananh.authservice.dto.mapper;

import com.tuananh.authservice.dto.response.ResUserDTO;
import com.tuananh.authservice.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserMapper {
    public ResUserDTO toResUserDTO(User user) {

        ResUserDTO.CompanyUser companyUser = ResUserDTO.CompanyUser.builder().id(user.getCompanyId()).name("null").build();
        ResUserDTO.RoleUser roleUser = ResUserDTO.RoleUser.builder().id(user.getRole().getId()).name(user.getRole().getName()).build();

        return ResUserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .dob(user.getDob())
                        .gender(user.getGender())
                        .address(user.getAddress())
                        .email(user.getEmail())
                        .company(companyUser)
                        .role(roleUser)
                        .createdAt(user.getCreatedAt())
                        .updatedAt(user.getUpdatedAt())
                        .build();
    }
}
