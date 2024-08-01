package com.tuananh.resumeservice.repository;

import com.tuananh.resumeservice.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long>,
        JpaSpecificationExecutor<Resume> {
//    boolean existsByName(String name);

//    Optional<Resume> findByName(String name);

    Optional<Resume> findById(long id);

}