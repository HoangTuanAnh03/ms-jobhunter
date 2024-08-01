package com.tuananh.resumeservice.service;//package com.tuananh.resumeservice.service;
//
//import com.tuananh.resumeservice.dto.request.ChangeStatusCompanyRequest;
//import com.tuananh.resumeservice.dto.request.CreateSkillRequest;
//import com.tuananh.resumeservice.dto.request.UpdateSkillRequest;
//import com.tuananh.resumeservice.dto.response.IntegrateInfoCompanyRes;
//import com.tuananh.resumeservice.dto.response.ResultPaginationDTO;
//import com.tuananh.resumeservice.entity.Company;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//
//public interface CompanyService {
//    /**
//     * @param name - Input companyName
//     * @return boolean indicating if the name already exited or not
//     */
//    boolean existByName(String name);
//
//    /**
//     * @param companyId - Input companyId
//     * @return IntegrateInfoCompanyRes Object on a given companyId
//     */
//    IntegrateInfoCompanyRes fetchById(long companyId);
//
//    /**
//     * @param spec     - filter
//     * @param pageable - page, size, sort(field,asc(desc))
//     * @return ResultPaginationDTO based on a given spec and pageable
//     */
//    ResultPaginationDTO getCompanies(Specification<Company> spec, Pageable pageable);
//
//    /**
//     * @param companyRequest - CreateSkillRequest Object
//     * @return Resume Object saved to database
//     */
//    Company create(CreateSkillRequest companyRequest);
//
//    /**
//     * @param updateCompanyRequest -UpdateSkillRequest Object
//     * @return Resume Object updated to database
//     */
//    Company update(UpdateSkillRequest updateCompanyRequest);
//
//    /**
//     * @param id - Input CompanyId
//     * @return boolean indicating if the delete of Role details is successful or not
//     */
//    boolean delete(long id);
//
//    Company changeStatus(long id, ChangeStatusCompanyRequest changeStatusCompanyRequest);
//}
