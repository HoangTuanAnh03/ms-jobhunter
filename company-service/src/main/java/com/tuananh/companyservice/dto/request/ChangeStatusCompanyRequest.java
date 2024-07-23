package com.tuananh.companyservice.dto.request;

import com.tuananh.companyservice.util.constant.CompanyEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangeStatusCompanyRequest {
    @Enumerated(EnumType.STRING)
    CompanyEnum status;
}