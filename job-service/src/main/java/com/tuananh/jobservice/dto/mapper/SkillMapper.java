package com.tuananh.jobservice.dto.mapper;

import com.tuananh.jobservice.dto.response.SkillResponse;
import com.tuananh.jobservice.entity.Skill;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SkillMapper {
    public SkillResponse toSkillResponse(Skill skill) {
        return SkillResponse.builder()
                .id(skill.getId())
                .name(skill.getName())
                .build();
    }
}

