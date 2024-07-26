package com.tuananh.jobservice.service.impl;

import com.tuananh.jobservice.advice.exception.DuplicateRecordException;
import com.tuananh.jobservice.advice.exception.ResourceNotFoundException;
import com.tuananh.jobservice.dto.mapper.SkillMapper;
import com.tuananh.jobservice.dto.request.CreateSkillRequest;
import com.tuananh.jobservice.dto.request.UpdateSkillRequest;
import com.tuananh.jobservice.dto.response.ResultPaginationDTO;
import com.tuananh.jobservice.dto.response.SkillResponse;
import com.tuananh.jobservice.entity.Skill;
import com.tuananh.jobservice.repository.SkillRepository;
import com.tuananh.jobservice.service.SkillService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkillServiceImpl implements SkillService {
    SkillRepository skillRepository;
    SkillMapper skillMapper;

    /**
     * @param id - skillId
     * @return Skill Object on a given skillId
     */
    @Override
    public Skill fetchById(long id) {
        return skillRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("SKILL", "skillId", id));
    }

    /**
     * @param spec     - filter
     * @param pageable - page, size, sort(field,asc(desc))
     * @return ResultPaginationDTO based on a given spec and pageable
     */
    @Override
    public ResultPaginationDTO fetchPagination(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pSkill = this.skillRepository.findAll(spec, pageable);


        var mt = ResultPaginationDTO.Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(pSkill.getTotalPages())
                .total(pSkill.getTotalElements())
                .build();

        return ResultPaginationDTO.builder()
                .meta(mt)
                .result(pSkill.getContent())
                .build();
    }

    /**
     * @param skillRequest - CreateSkillRequest Object
     * @return SkillResponse Object saved to database
     */
    @Override
    public SkillResponse create(CreateSkillRequest skillRequest) {
        if (skillRepository.existsByName(skillRequest.getName())) {
            throw new DuplicateRecordException("SKILL", "Name", skillRequest.getName());
        }

        Skill newSkill = Skill.builder()
                .name(skillRequest.getName())
                .build();
        skillRepository.save(newSkill);

        return skillMapper.toSkillResponse(newSkill);
    }

    /**
     * @param skillRequest -UpdateSkillRequest Object
     * @return SkillResponse Object updated to database
     */
    @Override
    public SkillResponse update(long id, UpdateSkillRequest skillRequest) {
        Skill skillById = skillRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("SKILL", "skillId", id));

        Skill skillByName = skillRepository.findByName(skillRequest.getName()).orElse(null);

        if (skillByName != null && skillByName.getId() != skillById.getId()
                && skillByName.getName().equals(skillRequest.getName())) {
            throw new DuplicateRecordException("SKILL", "Name", skillRequest.getName());
        }

        skillById.setName(skillRequest.getName());

        skillRepository.save(skillById);

        return skillMapper.toSkillResponse(skillById);
    }

    /**
     * @param id - Input skillId
     * @return boolean indicating if the delete of Skill details is successful or not
     */
    @Override
    public boolean delete(long id) {
        Skill skillById = skillRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("SKILL", "skillId", id));

        skillRepository.deleteById(skillById.getId());
        return true;
    }

}
