package com.tuananh.companyservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateCompanyRequest {
    @NotBlank(message = "Name cannot be blank")
    String name;

    @NotBlank(message = "Description cannot be blank")
    String description;

    @NotBlank(message = "Address cannot be blank")
    String address;

    @NotBlank(message = "Url cannot be blank")
    String url;

    String logo;

    String coverImage;

    @NotBlank(message = "Total Employee cannot be blank")
    long totalEmployee;
}