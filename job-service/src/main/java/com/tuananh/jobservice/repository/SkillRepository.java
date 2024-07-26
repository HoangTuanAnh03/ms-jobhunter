package com.tuananh.jobservice.repository;

import com.tuananh.jobservice.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long>,
        JpaSpecificationExecutor<Skill> {
    boolean existsByName(String name);

    Optional<Skill> findByName(String name);

    Optional<Skill> findById(long id);

    List<Skill> findByIdIn(Set<Long> skillIds);
}