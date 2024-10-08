package com.tuananh.jobservice.repository;

import com.tuananh.jobservice.entity.Job;
import com.tuananh.jobservice.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface JobRepository extends JpaRepository<Job, Long>,
        JpaSpecificationExecutor<Job> {
    boolean existsByName(String name);

    Optional<Job> findByName(String name);

    Optional<Job> findById(long id);

    List<Job> findByIdIn(Set<Long> ids);
}