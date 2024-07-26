package com.tuananh.authservice.dto.response;

import com.tuananh.authservice.util.constant.CompanyEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntegrateInfoCompanyRes {
    long id;

    String name;

    String description;

    String address;

    String url;

    String logo;

    String coverImage;

    long totalEmployee;

    long totalSubscriber;

    @Enumerated(EnumType.STRING)
    CompanyEnum status;
}