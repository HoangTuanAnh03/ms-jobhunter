package com.tuananh.resumeservice.dto.response;

import com.tuananh.resumeservice.util.constant.ResumeStateEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResumeResponse {
    long id;

    String url;

    String email;

    @Enumerated(EnumType.STRING)
    ResumeStateEnum status;

    UserInfo userInfo;

    CompanyInfo companyInfo;

    JobInfo jobInfo;
}