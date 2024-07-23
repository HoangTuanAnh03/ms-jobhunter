package com.tuananh.companyservice.dto.mapper;

import com.tuananh.companyservice.dto.response.IntegrateInfoCompanyRes;
import com.tuananh.companyservice.dto.response.SimpInfoCompanyRes;
import com.tuananh.companyservice.entity.Company;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CompanyMapper {
    public SimpInfoCompanyRes toSimpleInfoCompany(Company company) {
        return SimpInfoCompanyRes.builder()
                        .id(company.getId())
                        .name(company.getName())
                        .description(company.getDescription())
                        .logo(company.getLogo())
                        .coverImage(company.getCoverImage())
                        .build();
    }

    public IntegrateInfoCompanyRes toIntegrateInfoCompany (Company company) {
        return IntegrateInfoCompanyRes.builder()
                .id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .description(company.getDescription())
                .url(company.getUrl())
                .logo(company.getLogo())
                .coverImage(company.getCoverImage())
                .totalEmployee(company.getTotalEmployee())
                //call service client
                .totalSubscriber(0)
                .build();
    }
}

