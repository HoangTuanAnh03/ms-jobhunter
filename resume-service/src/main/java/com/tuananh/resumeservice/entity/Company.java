package com.tuananh.resumeservice.entity;

import com.tuananh.resumeservice.util.constant.CompanyEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company {
    long id;

    String name;

    String description;

    String address;

    String url;

    String logo;

    String coverImage;

    long totalEmployee;

    @Enumerated(EnumType.STRING)
    CompanyEnum status;

    LocalDateTime createdAt;

    String createdBy;

    LocalDateTime updatedAt;

    String updatedBy;
}
