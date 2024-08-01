package com.tuananh.companyservice.service;

import com.tuananh.companyservice.dto.request.ChangeStatusCompanyRequest;
import com.tuananh.companyservice.dto.request.CreateCompanyRequest;
import com.tuananh.companyservice.dto.request.UpdateCompanyRequest;
import com.tuananh.companyservice.dto.response.IntegrateInfoCompanyRes;
import com.tuananh.companyservice.dto.response.ResultPaginationDTO;
import com.tuananh.companyservice.entity.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Set;

public interface CompanyService {
    /**
     * @param name - Input companyName
     * @return boolean indicating if the name already exited or not
     */
    boolean existByName(String name);

    /**
     * @param companyId - Input companyId
     * @return IntegrateInfoCompanyRes Object on a given companyId
     */
    IntegrateInfoCompanyRes fetchById(long companyId);

    /**
     * @param spec     - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    ResultPaginationDTO getCompanies(Specification<Company> spec, Pageable pageable);

    /**
     * @param companyRequest - CreateCompanyRequest Object
     * @return Company Object saved to database
     */
    Company create(CreateCompanyRequest companyRequest);

    /**
     * @param updateCompanyRequest -UpdateCompanyRequest Object
     * @return Company Object updated to database
     */
    Company update(UpdateCompanyRequest updateCompanyRequest);

    /**
     * @param id - Input CompanyId
     * @return boolean indicating if the delete of Role details is successful or not
     */
    boolean delete(long id);

    Company changeStatus(long id, ChangeStatusCompanyRequest changeStatusCompanyRequest);

    List<Company> fetchByIdIn(List<Long> ids);
}
