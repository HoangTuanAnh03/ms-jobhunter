package com.tuananh.companyservice.repository;

import com.tuananh.companyservice.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>,
        JpaSpecificationExecutor<Company> {
    boolean existsByName(String name);

    Optional<Company> findByName(String name);

    Optional<Company> findById(long id);
}