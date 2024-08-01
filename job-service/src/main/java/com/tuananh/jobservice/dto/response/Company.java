package com.tuananh.jobservice.dto.response;

import com.tuananh.jobservice.util.constant.CompanyEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
}