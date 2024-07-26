package com.tuananh.companyservice.service.impl;

import com.tuananh.companyservice.advice.exception.ResourceNotFoundException;
import com.tuananh.companyservice.dto.mapper.CompanyMapper;
import com.tuananh.companyservice.dto.request.ChangeStatusCompanyRequest;
import com.tuananh.companyservice.dto.request.CreateCompanyRequest;
import com.tuananh.companyservice.dto.request.UpdateCompanyRequest;
import com.tuananh.companyservice.dto.response.IntegrateInfoCompanyRes;
import com.tuananh.companyservice.dto.response.ResultPaginationDTO;
import com.tuananh.companyservice.entity.Company;
import com.tuananh.companyservice.repository.CompanyRepository;
import com.tuananh.companyservice.service.CompanyService;
import com.tuananh.companyservice.service.client.AuthClient;
import com.tuananh.companyservice.util.constant.CompanyEnum;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyServiceImpl implements CompanyService {
    CompanyRepository companyRepository;
    CompanyMapper companyMapper;
    AuthClient authClient;

    /**
     * @param name - Input companyName
     * @return boolean indicating if the name already exited or not
     */
    @Override
    public boolean existByName(String name) {
        return this.companyRepository.existsByName(name);
    }

    /**
     * @param companyId - Input companyId
     * @return IntegrateInfoCompanyRes Object on a given companyId
     */
    @Override
    public IntegrateInfoCompanyRes fetchById(long companyId) {
        Company company = this.companyRepository.findById(companyId).orElse(null);

        if (company == null) {
            throw new ResourceNotFoundException("Company", "CompanyId", companyId);
        }

        return companyMapper.toIntegrateInfoCompany(company);
    }

    /**
     * @param spec     - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    @Override
    public ResultPaginationDTO getCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pCompany = this.companyRepository.findAll(spec, pageable);

        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pCompany.getTotalPages());
        mt.setTotal(pCompany.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pCompany.getContent().stream().map(companyMapper::toSimpleInfoCompany));
        return rs;
    }

    /**
     * @param companyRequest - CreateCompanyRequest Object
     * @return Company Object saved to database
     */
//    @Transactional
    @Override
    public Company create(CreateCompanyRequest companyRequest) {
//        if (existByName(companyRequest.getName())) {
//            throw new DuplicateRecordException("Company", "Name", companyRequest.getName());
//        }

        Company newCompany = Company.builder()
                .name(companyRequest.getName())
                .description(companyRequest.getDescription())
                .address(companyRequest.getAddress())
                .url(companyRequest.getUrl())
                .logo(companyRequest.getLogo())
                .coverImage(companyRequest.getCoverImage())
                .totalEmployee(companyRequest.getTotalEmployee())
                .status(CompanyEnum.REVIEWING)
                .build();

        Company savedCompany = this.companyRepository.save(newCompany);

        // update Role=HR for user create company
        authClient.updateHR(savedCompany.getId());

        return savedCompany;
    }

    /**
     * @param updateCompanyRequest -UpdateCompanyRequest Object
     * @return Company Object updated to database
     */
    @Override
    public Company update(UpdateCompanyRequest updateCompanyRequest) {
        long companyId = authClient.getInfo().getData().getCompany().getId();

        Company currentCompany = companyRepository.findById(companyId).orElseThrow(
                () -> new ResourceNotFoundException("Company", "CompanyId", companyId)
        );

        currentCompany.setName(updateCompanyRequest.getName());
        currentCompany.setDescription(updateCompanyRequest.getDescription());
        currentCompany.setAddress(updateCompanyRequest.getAddress());
        currentCompany.setCoverImage(updateCompanyRequest.getCoverImage());
        currentCompany.setLogo(updateCompanyRequest.getLogo());
        currentCompany.setUrl(updateCompanyRequest.getUrl());
        currentCompany.setTotalEmployee(updateCompanyRequest.getTotalEmployee());

        currentCompany = this.companyRepository.save(currentCompany);
        return currentCompany;
    }

    /**
     * @param id - Input CompanyId
     * @return boolean indicating if the delete of Role details is successful or not
     */
    @Override
    public boolean delete(long id) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Company", "CompanyId", id)
        );
        this.companyRepository.deleteById(company.getId());
        return true;
    }

    /**
     * @param id                         - Input companyId
     * @param changeStatusCompanyRequest - Input ChangeStatusCompanyRequest Object
     * @return Company Object
     */
    @Override
    public Company changeStatus(long id, ChangeStatusCompanyRequest changeStatusCompanyRequest) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Company", "CompanyId", id)
        );
        company.setStatus(changeStatusCompanyRequest.getStatus());
        return companyRepository.save(company);
    }


}
