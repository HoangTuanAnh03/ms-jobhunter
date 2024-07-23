package com.tuananh.companyservice.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompanyRequest {
    String name;

    String description;

    String address;

    String url;

    String logo;

    String coverImage;

    long totalEmployee;
}