package com.tuananh.jobservice.service;

import com.tuananh.jobservice.dto.request.CreateSkillRequest;
import com.tuananh.jobservice.dto.request.UpdateSkillRequest;
import com.tuananh.jobservice.dto.response.ResultPaginationDTO;
import com.tuananh.jobservice.dto.response.SkillResponse;
import com.tuananh.jobservice.entity.Skill;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface SkillService {
    /**
     * @param id - skillId
     * @return Skill Object on a given skillId
     */
    Skill fetchById(long id);

    /**
     * @param spec     - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    ResultPaginationDTO fetchPagination(Specification<Skill> spec, Pageable pageable);

    /**
     * @param skillRequest - CreateSkillRequest Object
     * @return SkillResponse Object saved to database
     */
    SkillResponse create(CreateSkillRequest skillRequest);

    /**
     * @param skillRequest -UpdateSkillRequest Object
     * @return SkillResponse Object updated to database
     */
    SkillResponse update(long id, UpdateSkillRequest skillRequest);

    /**
     * @param id - Input skillId
     * @return boolean indicating if the delete of Skill details is successful or not
     */
    boolean delete(long id);

}
