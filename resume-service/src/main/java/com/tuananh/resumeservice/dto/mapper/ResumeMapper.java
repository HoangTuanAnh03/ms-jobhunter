package com.tuananh.resumeservice.dto.mapper;

import com.tuananh.resumeservice.dto.response.*;
import com.tuananh.resumeservice.entity.Job;
import com.tuananh.resumeservice.entity.Resume;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResumeMapper {
    public ResumeResponse toResumeResponse(Resume resume, UserResponse userResponse, Job job) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .url(resume.getUrl())
                .email(resume.getEmail())
                .status(resume.getStatus())
                .userInfo(UserInfo.builder()
                        .id(userResponse.getId())
                        .name(userResponse.getName())
                        .email(userResponse.getEmail())
                        .build())
                .companyInfo(CompanyInfo.builder()
                        .id(userResponse.getCompany().getId())
                        .name(userResponse.getCompany().getName())
                        .build())
                .jobInfo(JobInfo.builder()
                        .id(job.getId())
                        .name(job.getName())
                        .build())
                .build();
    }

//    public IntegrateInfoCompanyRes toIntegrateInfoCompany (Company company) {
//        return IntegrateInfoCompanyRes.builder()
//                .id(company.getId())
//                .name(company.getName())
//                .address(company.getAddress())
//                .description(company.getDescription())
//                .url(company.getUrl())
//                .logo(company.getLogo())
//                .coverImage(company.getCoverImage())
//                .totalEmployee(company.getTotalEmployee())
//                //call service client
//                .totalSubscriber(0)
//                .build();
//    }
}

